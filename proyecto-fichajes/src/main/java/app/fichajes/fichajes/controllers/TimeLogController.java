package app.fichajes.fichajes.controllers;

import app.fichajes.fichajes.models.dtos.request.CreateTimeLogRequestDTO;
import app.fichajes.fichajes.services.TimeLogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/timelogs")
public class TimeLogController {

    private final TimeLogService timeLogService;

    @Autowired
    public TimeLogController(TimeLogService timeLogService) {
        this.timeLogService = timeLogService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTimeLog(@Valid @RequestBody CreateTimeLogRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(timeLogService.createTimeLog(dto));
    }

    @GetMapping("/fetch")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(timeLogService.getAll());
    }
}