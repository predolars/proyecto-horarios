package app.fichajes.fichajes.controllers;

import app.fichajes.fichajes.models.dtos.request.TimeLogRequestDTO;
import app.fichajes.fichajes.models.dtos.response.TimeLogResponseDTO;
import app.fichajes.fichajes.services.TimeLogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/timelogs")
public class TimeLogController {

    private final TimeLogService timeLogService;

    @Autowired
    public TimeLogController(TimeLogService timeLogService) {
        this.timeLogService = timeLogService;
    }

    @PostMapping("/clock-in")
    public ResponseEntity<TimeLogResponseDTO> clockIn(@Valid @RequestBody TimeLogRequestDTO dto) {
        TimeLogResponseDTO response = timeLogService.performClockIn(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/clock-out")
    public ResponseEntity<TimeLogResponseDTO> clockOut(@Valid @RequestBody TimeLogRequestDTO dto) {
        TimeLogResponseDTO response = timeLogService.performClockOut(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TimeLogResponseDTO>> getAll() {
        return ResponseEntity.ok(timeLogService.getAll());
    }
}
