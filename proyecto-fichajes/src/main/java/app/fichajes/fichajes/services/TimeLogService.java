package app.fichajes.fichajes.services;

import app.fichajes.fichajes.exceptions.GenericException;
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

    private TimeLog createTimeLog(Assignment assignment, TimeLogType timeLogType, TimeLogRequestDTO dto) {
        TimeLog newTimeLog = new TimeLog();
        newTimeLog.setAssignment(assignment);
        newTimeLog.setDateTimeTimelog(LocalDateTime.now());
        newTimeLog.setTimeLogType(timeLogType);
        newTimeLog.setLatitudTimelog(dto.getLatitude());
        newTimeLog.setLongitudeTimelog(dto.getLongitude());
        return newTimeLog;
    }

    @Transactional
    public TimeLogResponseDTO performClockIn(TimeLogRequestDTO dto) {
        Assignment assignment = findAssignmentById(dto.getAssignmentId());

        Optional<TimeLog> lastTimeLogOpt = findLastTimeLog(assignment);

        // Tu validación es correcta. Si existe un último fichaje y NO es FINISH, es un error.
        if (lastTimeLogOpt.isPresent() && lastTimeLogOpt.get().getTimeLogType() != TimeLogType.FINISH) {
            throw new GenericException("User has already clocked in. Must clock out first.");
        }

        TimeLog newTimeLog = createTimeLog(assignment, TimeLogType.START, dto);
        // Correcto: START no resume horas.

        TimeLog savedTimeLog = timeLogRepository.save(newTimeLog);
        return modelMapper.map(savedTimeLog, TimeLogResponseDTO.class);
    }

    @Transactional
    public TimeLogResponseDTO performPause(TimeLogRequestDTO dto) {
        Assignment assignment = findAssignmentById(dto.getAssignmentId());

        TimeLog lastEntry = findLastTimeLog(assignment)
                .orElseThrow(() -> new GenericException("Cannot pause without having clocked in first."));

        // Validación correcta
        TimeLogType lastType = lastEntry.getTimeLogType();
        if (lastType != TimeLogType.START && lastType != TimeLogType.RESUME) {
            throw new GenericException("Cannot pause. The user is already paused, or has finished the shift.");
        }

        TimeLog newTimeLog = createTimeLog(assignment, TimeLogType.PAUSE, dto);

        calculateSummarizedHours(lastEntry, newTimeLog);

        TimeLog savedTimeLog = timeLogRepository.save(newTimeLog);
        return modelMapper.map(savedTimeLog, TimeLogResponseDTO.class);
    }

    @Transactional
    public TimeLogResponseDTO performResume(TimeLogRequestDTO dto) {
        Assignment assignment = findAssignmentById(dto.getAssignmentId());

        TimeLog lastEntry = findLastTimeLog(assignment)
                .orElseThrow(() -> new GenericException("Cannot resume. No previous time log found."));

        // Validación correcta
        if (lastEntry.getTimeLogType() != TimeLogType.PAUSE) {
            throw new GenericException("Cannot resume. The user is not currently paused.");
        }

        TimeLog newTimeLog = createTimeLog(assignment, TimeLogType.RESUME, dto);

        // Correcto: arrastramos el total acumulado en el PAUSE.
        newTimeLog.setSummarizedHours(lastEntry.getSummarizedHours());

        TimeLog savedTimeLog = timeLogRepository.save(newTimeLog);
        return modelMapper.map(savedTimeLog, TimeLogResponseDTO.class);
    }

    @Transactional
    public TimeLogResponseDTO performClockOut(TimeLogRequestDTO dto) {
        Assignment assignment = findAssignmentById(dto.getAssignmentId());

        TimeLog lastEntry = findLastTimeLog(assignment)
                .orElseThrow(() -> new GenericException("Cannot clock out without having clocked in first."));

        // TODO VALIDAR MAS DE DOS HORAS
        if (lastEntry.getTimeLogType() != TimeLogType.START && lastEntry.getTimeLogType() != TimeLogType.RESUME) {
            throw new GenericException("The last time log was not a START or RESUME. Cannot clock out if paused or already finished.");
        }

        TimeLog newTimeLog = createTimeLog(assignment, TimeLogType.FINISH, dto);

        calculateSummarizedHours(lastEntry, newTimeLog);

        TimeLog savedTimeLog = timeLogRepository.save(newTimeLog);
        return modelMapper.map(savedTimeLog, TimeLogResponseDTO.class);
    }

    private void calculateSummarizedHours(TimeLog lastEntry, TimeLog newTimeLog) {
        Duration duration = Duration.between(lastEntry.getDateTimeTimelog(), newTimeLog.getDateTimeTimelog());
        double hours = duration.toHours() / 3600000.0; // 1000ms * 60s * 60m
        double previousHours = Optional.ofNullable(lastEntry.getSummarizedHours()).orElse(0.0);

        newTimeLog.setSummarizedHours(previousHours + hours);
    }

    private Assignment findAssignmentById(Long assignmentId) {
        return assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + assignmentId));
    }

    private Optional<TimeLog> findLastTimeLog(Assignment assignment) {
        return timeLogRepository.findTopByAssignmentOrderByDateTimeTimelogDesc(assignment);
    }

    @Transactional(readOnly = true)
    public List<TimeLogResponseDTO> getAll() {
        return timeLogRepository.findAll().stream()
                .map(t -> modelMapper.map(t, TimeLogResponseDTO.class))
                .toList();
    }
}