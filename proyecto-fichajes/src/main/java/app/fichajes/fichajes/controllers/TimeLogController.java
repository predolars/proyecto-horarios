package app.fichajes.fichajes.controllers;

import app.fichajes.fichajes.models.dtos.request.TimeLogRequestDTO;
import app.fichajes.fichajes.models.dtos.response.TimeLogResponseDTO;
import app.fichajes.fichajes.models.enums.TimeLogType;
import app.fichajes.fichajes.services.TimeLogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/timelogs")
public class TimeLogController {

    private final TimeLogService timeLogService;

    @Autowired
    public TimeLogController(TimeLogService timeLogService) {
        this.timeLogService = timeLogService;
    }

    @PostMapping("/clock-in")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TimeLogResponseDTO> clockIn(@Valid @RequestBody TimeLogRequestDTO dto) {
        TimeLogResponseDTO response = timeLogService.performClockIn(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/pause")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TimeLogResponseDTO> pause(@Valid @RequestBody TimeLogRequestDTO dto) {
        TimeLogResponseDTO response = timeLogService.performPause(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/resume")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TimeLogResponseDTO> resume(@Valid @RequestBody TimeLogRequestDTO dto) {
        TimeLogResponseDTO response = timeLogService.performResume(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/clock-out")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TimeLogResponseDTO> clockOut(@Valid @RequestBody TimeLogRequestDTO dto) {
        TimeLogResponseDTO response = timeLogService.performClockOut(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<TimeLogResponseDTO>> getAll() {
        return ResponseEntity.ok(timeLogService.getAll());
    }

    @GetMapping("/my/{assignmentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<TimeLogResponseDTO>> getMyTimeLogs(
            @PathVariable Long assignmentId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) TimeLogType type) {
        List<TimeLogResponseDTO> timeLogs = timeLogService.getMyTimeLogsInfo(assignmentId, startDate, endDate, type);
        return ResponseEntity.ok(timeLogs);
    }

    @PutMapping("/approvals/{assignmentId}")
    @PreAuthorize("hasAnyRole('JEFE', 'ENCARGADO')")
    public ResponseEntity<Object> approveTimeLogs(@RequestBody Set<Long> timelogIds, @PathVariable Long assignmentId) {
        timeLogService.approveTimeLogs(timelogIds, assignmentId);
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    /**
     * Actualiza la hora de un fichaje (para correcciones de mánager).
     * Requiere un DTO (ej. TimeLogEditRequestDTO) que contenga el nuevo LocalDateTime.
     * Requiere rol de gestión.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('JEFE', 'ENCARGADO', 'ADMIN')")
    public ResponseEntity<TimeLogResponseDTO> updateTimeLog(@PathVariable Long id, @RequestParam LocalDateTime updateDateTime) {
        TimeLogResponseDTO response = timeLogService.updateTimeLog(id, updateDateTime);
        return ResponseEntity.ok(response);
    }

}
