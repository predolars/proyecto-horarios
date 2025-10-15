package app.fichajes.fichajes.services;

import app.fichajes.fichajes.exceptions.ResourceNotFoundException;
import app.fichajes.fichajes.models.dtos.request.TimeLogRequestDTO;
import app.fichajes.fichajes.models.dtos.response.TimeLogResponseDTO;
import app.fichajes.fichajes.models.entities.Assignment;
import app.fichajes.fichajes.models.entities.TimeLog;
import app.fichajes.fichajes.models.enums.TimeLogType;
import app.fichajes.fichajes.repositories.AssignmentRepository;
import app.fichajes.fichajes.repositories.TimeLogRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TimeLogService {

    private final TimeLogRepository timeLogRepository;
    private final AssignmentRepository assignmentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public TimeLogService(TimeLogRepository timeLogRepository, AssignmentRepository assignmentRepository, ModelMapper modelMapper) {
        this.timeLogRepository = timeLogRepository;
        this.assignmentRepository = assignmentRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public TimeLogResponseDTO performClockIn(TimeLogRequestDTO dto) {
        Assignment assignment = findAssignmentById(dto.getAssignmentId());

        Optional<TimeLog> lastTimeLogOpt = timeLogRepository.findTopByAssignmentOrderByDateTimeTimelogDesc(assignment);
        if (lastTimeLogOpt.isPresent() && lastTimeLogOpt.get().getTimeLogType() == TimeLogType.START) {
            throw new IllegalStateException("User has already clocked in. Must clock out first.");
        }
        TimeLog newTimeLog = new TimeLog();
        newTimeLog.setAssignment(assignment);
        newTimeLog.setDateTimeTimelog(LocalDateTime.now());
        newTimeLog.setTimeLogType(TimeLogType.START);
        newTimeLog.setLatitudTimelog(dto.getLatitude());
        newTimeLog.setLongitudeTimelog(dto.getLongitude());

        TimeLog savedTimeLog = timeLogRepository.save(newTimeLog);
        return modelMapper.map(savedTimeLog, TimeLogResponseDTO.class);
    }

    @Transactional
    public TimeLogResponseDTO performClockOut(TimeLogRequestDTO dto) {
        Assignment assignment = findAssignmentById(dto.getAssignmentId());

        TimeLog lastEntry = timeLogRepository.findTopByAssignmentOrderByDateTimeTimelogDesc(assignment)
                .orElseThrow(() -> new IllegalStateException("Cannot clock out without having clocked in first."));

        if (lastEntry.getTimeLogType() != TimeLogType.START) {
            throw new IllegalStateException("The last time log was not a clock-in. Cannot clock out.");
        }

        TimeLog newTimeLog = new TimeLog();
        newTimeLog.setAssignment(assignment);
        newTimeLog.setDateTimeTimelog(LocalDateTime.now());
        newTimeLog.setTimeLogType(TimeLogType.FINISH);
        newTimeLog.setLatitudTimelog(dto.getLatitude());
        newTimeLog.setLongitudeTimelog(dto.getLongitude());

        Duration duration = Duration.between(lastEntry.getDateTimeTimelog(), newTimeLog.getDateTimeTimelog());
        double hours = duration.toMinutes() / 60.0;
        newTimeLog.setSummarizedHours(hours);

        TimeLog savedTimeLog = timeLogRepository.save(newTimeLog);
        return modelMapper.map(savedTimeLog, TimeLogResponseDTO.class);
    }

    private Assignment findAssignmentById(Long assignmentId) {
        return assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + assignmentId));
    }


    public List<TimeLogResponseDTO> getAll() {
        return timeLogRepository.findAll().stream()
                .map(t -> modelMapper.map(t, TimeLogResponseDTO.class))
                .toList();
    }
}