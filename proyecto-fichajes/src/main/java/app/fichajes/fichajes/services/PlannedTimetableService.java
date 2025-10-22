package app.fichajes.fichajes.services;

import app.fichajes.fichajes.exceptions.FieldDataAlreadyExistsException;
import app.fichajes.fichajes.exceptions.GenericException;
import app.fichajes.fichajes.exceptions.ResourceNotFoundException;
import app.fichajes.fichajes.models.dtos.request.TimetableRequestDTO;
import app.fichajes.fichajes.models.dtos.response.PlannedTimetableResponseDTO;
import app.fichajes.fichajes.models.entities.Assignment;
import app.fichajes.fichajes.models.entities.PlannedTimetable;
import app.fichajes.fichajes.repositories.AssignmentRepository;
import app.fichajes.fichajes.repositories.PlannedTimetableRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class PlannedTimetableService {

    private final PlannedTimetableRepository timetableRepository;
    private final AssignmentRepository assignmentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PlannedTimetableService(PlannedTimetableRepository timetableRepository, AssignmentRepository assignmentRepository, ModelMapper modelMapper) {
        this.timetableRepository = timetableRepository;
        this.assignmentRepository = assignmentRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public List<PlannedTimetableResponseDTO> createTimetables(List<TimetableRequestDTO> timetablesDTOs) {

        // Previous validations in a functional style

        // We group by employee. This is declarative and efficient.
        Map<Long, List<TimetableRequestDTO>> groupedByAssignment = timetablesDTOs.stream()
                .collect(Collectors.groupingBy(TimetableRequestDTO::getAssignmentId));

        // Validate each group of schedules. We use forEach because we need to throw exceptions in case of failure.
        groupedByAssignment.values().forEach(employeeTimetables -> {
            // Sort to reliably detect internal overlaps
            employeeTimetables.sort(Comparator.comparing(TimetableRequestDTO::getPlannedDateTimeStart));

            // Use an IntStream to iterate with indexes. It is more functional than a classic for-loop
            // and allows us to compare an element with the next one.
            IntStream.range(0, employeeTimetables.size()).forEach(timetableIndex -> {
                TimetableRequestDTO current = employeeTimetables.get(timetableIndex);

                // Date consistency validation
                validateTimetableDates(current);

                // Overlap check with the next schedule in the BATCH
                if (timetableIndex + 1 < employeeTimetables.size()) {
                    TimetableRequestDTO next = employeeTimetables.get(timetableIndex + 1);
                    if (current.getPlannedDateTimeEnd().isAfter(next.getPlannedDateTimeStart())) {
                        throw new FieldDataAlreadyExistsException(
                                "Error in batch: There are overlapping schedules for assignment ID: " + current.getAssignmentId() + " at dates: " + next.getPlannedDateTimeStart()
                        );
                    }
                }

                // Overlap check with the DATABASE
                if (!timetableRepository.findOverlappingTimetables(
                        current.getAssignmentId(),
                        current.getPlannedDateTimeStart(),
                        current.getPlannedDateTimeEnd()
                ).isEmpty()) {
                    throw new FieldDataAlreadyExistsException("The employee (ID: " + current.getAssignmentId() + ") already has an overlapping for that day: " + current.getPlannedDateTimeStart() + ".");
                }
            });
        });

        // If all validations pass, we transform DTOs to Entities

        // Pre-load the necessary 'assignments' to avoid multiple calls in the stream.
        Set<Long> assignmentIds = timetablesDTOs.stream()
                .flatMap(dto -> Stream.of(dto.getAssignmentId(), dto.getCreatedByAssignmentId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, Assignment> assignmentMap = assignmentRepository.findAllById(assignmentIds).stream()
                .collect(Collectors.toMap(Assignment::getId, Function.identity()));


        List<PlannedTimetable> timetablesToSave = timetablesDTOs.stream()
                .map(dto -> {
                    Assignment assignment = Optional.ofNullable(assignmentMap.get(dto.getAssignmentId()))
                            .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + dto.getAssignmentId()));

                    PlannedTimetable timetable = modelMapper.map(dto, PlannedTimetable.class);
                    timetable.setAssignment(assignment);

                    if (dto.getCreatedByAssignmentId() != null) {
                        Assignment createdBy = Optional.ofNullable(assignmentMap.get(dto.getCreatedByAssignmentId()))
                                .orElseThrow(() -> new ResourceNotFoundException("Creator assignment not found with id: " + dto.getCreatedByAssignmentId()));
                        timetable.setCreateByAssignment(createdBy);
                    }

                    return timetable;
                })
                .toList();

        // --- STEP 3: We save and map the response ---

        return timetableRepository.saveAll(timetablesToSave).stream()
                .map(savedTimetable -> modelMapper.map(savedTimetable, PlannedTimetableResponseDTO.class))
                .toList();
    }

    // Helper function to keep the code clean
    private void validateTimetableDates(TimetableRequestDTO dto) {
        if (dto.getPlannedDateTimeStart().isAfter(dto.getPlannedDateTimeEnd()) || dto.getPlannedDateTimeStart().isEqual(dto.getPlannedDateTimeEnd())) {
            throw new GenericException("The start date must be before the end date for the schedule of assignment ID: " + dto.getAssignmentId());
        }

        if (dto.getPlannedDateTimeStart().isBefore(LocalDateTime.now())) {
            throw new GenericException("Schedules cannot be planned for past dates for assignment ID: " + dto.getAssignmentId());
        }
    }

    @Transactional(readOnly = true)
    public List<PlannedTimetableResponseDTO> getAll() {
        return timetableRepository.findAll().stream()
                .map(t -> modelMapper.map(t, PlannedTimetableResponseDTO.class))
                .toList();
    }
}