package app.fichajes.fichajes.services;

import app.fichajes.fichajes.exceptions.ResourceNotFoundException;
import app.fichajes.fichajes.models.dtos.request.CreateTimetableRequestDTO;
import app.fichajes.fichajes.models.dtos.response.PlannedTimetableResponseDTO;
import app.fichajes.fichajes.models.entities.Assignment;
import app.fichajes.fichajes.models.entities.PlannedTimetable;
import app.fichajes.fichajes.repositories.AssignmentRepository;
import app.fichajes.fichajes.repositories.PlannedTimetableRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public PlannedTimetableResponseDTO createTimetable(CreateTimetableRequestDTO dto) {
        Assignment assignment = assignmentRepository.findById(dto.getAssignmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Asignación no encontrada con id: " + dto.getAssignmentId()));
        
        PlannedTimetable timetable = new PlannedTimetable();
        timetable.setAssignment(assignment);
        timetable.setPlannedDateTimeStart(dto.getPlannedDateTimeStart());
        timetable.setPlannedDateTimeEnd(dto.getPlannedDateTimeEnd());

        // Lógica para asignar quién lo crea (si se proporciona)
        if(dto.getCreatedByAssignmentId() != null){
            Assignment createdBy = assignmentRepository.findById(dto.getCreatedByAssignmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Asignación del creador no encontrada con id: " + dto.getCreatedByAssignmentId()));
            timetable.setCreateByAssignment(createdBy);
        }

        PlannedTimetable savedTimetable = timetableRepository.save(timetable);
        return modelMapper.map(savedTimetable, PlannedTimetableResponseDTO.class);
    }

    public List<PlannedTimetableResponseDTO> getAll() {
        return timetableRepository.findAll().stream()
                .map(t -> modelMapper.map(t, PlannedTimetableResponseDTO.class))
                .collect(Collectors.toList());
    }
}