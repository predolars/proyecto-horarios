package app.fichajes.fichajes.services;

import app.fichajes.fichajes.exceptions.ResourceNotFoundException;
import app.fichajes.fichajes.models.dtos.request.CreateTimeLogRequestDTO;
import app.fichajes.fichajes.models.dtos.response.TimeLogResponseDTO;
import app.fichajes.fichajes.models.entities.Assignment;
import app.fichajes.fichajes.models.entities.TimeLog;
import app.fichajes.fichajes.repositories.AssignmentRepository;
import app.fichajes.fichajes.repositories.TimeLogRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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

    public TimeLogResponseDTO createTimeLog(CreateTimeLogRequestDTO dto) {
        Assignment assignment = assignmentRepository.findById(dto.getAssignmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Asignación no encontrada con id: " + dto.getAssignmentId()));

        TimeLog timeLog = new TimeLog();
        timeLog.setAssignment(assignment);
        timeLog.setDateTimeTimelog(LocalDateTime.now());
        timeLog.setTimeLogType(dto.getTimeLogType());
        timeLog.setLatitudTimelog(dto.getLatitude());
        timeLog.setLongitudeTimelog(dto.getLongitude());

        TimeLog savedTimeLog = timeLogRepository.save(timeLog);
        return modelMapper.map(savedTimeLog, TimeLogResponseDTO.class);
    }

    public List<TimeLogResponseDTO> getAll() {
        return timeLogRepository.findAll().stream()
                .map(t -> modelMapper.map(t, TimeLogResponseDTO.class))
                .collect(Collectors.toList());
    }
}