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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TimeLogService {

    private final TimeLogRepository timeLogRepository;
    private final AssignmentRepository assignmentRepository;
    private final ModelMapper modelMapper;

    private static final String EXCEPTION_MESSAGE = "Assignment not found with id: ";

    @Autowired
    public TimeLogService(TimeLogRepository timeLogRepository, AssignmentRepository assignmentRepository, ModelMapper modelMapper) {
        this.timeLogRepository = timeLogRepository;
        this.assignmentRepository = assignmentRepository;
        this.modelMapper = modelMapper;
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

        newTimeLog.setSummarizedSeconds(calculateSummarizedSeconds(lastEntry, newTimeLog));
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
        newTimeLog.setSummarizedSeconds(lastEntry.getSummarizedSeconds());

        TimeLog savedTimeLog = timeLogRepository.save(newTimeLog);
        return modelMapper.map(savedTimeLog, TimeLogResponseDTO.class);
    }

    @Transactional
    public TimeLogResponseDTO performClockOut(TimeLogRequestDTO dto) {
        Assignment assignment = findAssignmentById(dto.getAssignmentId());

        TimeLog lastEntry = findLastTimeLog(assignment)
                .orElseThrow(() -> new GenericException("Cannot clock out without having clocked in first."));

        TimeLog newTimeLog = createTimeLog(assignment, TimeLogType.FINISH, dto);

        newTimeLog.setSummarizedSeconds(calculateSummarizedSeconds(lastEntry, newTimeLog));

        // 120 minutos * 60 segundos = 7200
        if (newTimeLog.getSummarizedSeconds() < 7200L) {
            throw new GenericException("The total work duration must be 2 hours (120 minutes) or more");
        }

        if (lastEntry.getTimeLogType() != TimeLogType.START && lastEntry.getTimeLogType() != TimeLogType.RESUME) {
            throw new GenericException("The last time log was not a START or RESUME. Cannot clock out if paused or already finished.");
        }

        TimeLog savedTimeLog = timeLogRepository.save(newTimeLog);
        return modelMapper.map(savedTimeLog, TimeLogResponseDTO.class);
    }

    private Assignment findAssignmentById(Long assignmentId) {
        return assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException(EXCEPTION_MESSAGE + assignmentId));
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

    @Transactional(readOnly = true)
    public List<TimeLogResponseDTO> getMyTimeLogsInfo(Long assignmentId, LocalDate startDate, LocalDate endDate, TimeLogType type) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException(EXCEPTION_MESSAGE + assignmentId));

        if (!currentUserEmail.equalsIgnoreCase(assignment.getUser().getEmail())) {
            throw new AccessDeniedException("You do not have permission to view timetables for this assignment.");
        }

        // 2. LÓGICA DE FILTRADO
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;

        if (startDate != null) {
            startDateTime = startDate.atStartOfDay(); // Ej: 2025-10-28 -> 2025-10-28T00:00:00
        }

        if (endDate != null) {
            endDateTime = endDate.atTime(LocalTime.MAX); // Ej: 2025-10-28 -> 2025-10-28T23:59:59...
        }

        // Filtrar solo por 'startDate' (para ver un solo día)
        if (startDate != null && endDate == null) {
            endDateTime = startDate.atTime(LocalTime.MAX); // Fin de ese mismo día
        }

        // 3. LLAMADA AL REPOSITORIO
        List<TimeLog> timeLogs = timeLogRepository.findMyTimeLogsFiltered(
                assignment, startDateTime, endDateTime, type
        );

        // 4. MAPEO Y RESPUESTA
        return timeLogs.stream()
                .map(timeLog -> modelMapper.map(timeLog, TimeLogResponseDTO.class))
                .toList();
    }

    @Transactional
    public void approveTimeLogs(Set<Long> timelogs, Long assignmentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Assignment managerAssignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException(EXCEPTION_MESSAGE + assignmentId));

        if (!authentication.getName().equalsIgnoreCase(managerAssignment.getUser().getEmail())) {
            throw new AccessDeniedException("You have not permission to make this action.");
        }

        List<TimeLog> timeLogs = timeLogRepository.findAllById(timelogs);

        if (timeLogs.size() != timelogs.size()) {
            throw new ResourceNotFoundException("Time logs in conflict. Some time log is missing. Try to update again");
        }

        timeLogs.forEach(timeLog -> {
            if (!timeLog.getAssignment().getCompany().equals(managerAssignment.getCompany())) {
                throw new AccessDeniedException("You do not have permission to approve timelogs from other companies.");
            }
            // No hace falta save(), @Transactional se encarga.
            timeLog.setApprovedByAssignment(managerAssignment);
        });
    }

    @Transactional
    public TimeLogResponseDTO updateTimeLog(Long id, LocalDateTime updatedDateTime) {
        // 1. Buscar el fichaje
        TimeLog timeLog = timeLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TimeLog not found with id: " + id));

        // 2. Actualizar el timestamp
        // ADVERTENCIA: ¡Esto NO recalcula las 'summarizedHours' automáticamente!
        timeLog.setDateTimeTimelog(updatedDateTime);

        // 3. Dejar que @Transactional guarde
        return modelMapper.map(timeLog, TimeLogResponseDTO.class);
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

    private long calculateSummarizedSeconds(TimeLog lastEntry, TimeLog newTimeLog) {
        Duration duration = Duration.between(lastEntry.getDateTimeTimelog(), newTimeLog.getDateTimeTimelog());

        long seconds = duration.toSeconds();
        long previousSeconds = Optional.ofNullable(lastEntry.getSummarizedSeconds()).orElse(0L);

        return previousSeconds + seconds;

    }
}