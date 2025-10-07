package app.fichajes.fichajes.controllers;

import app.fichajes.fichajes.models.dtos.request.CreateTimetableRequestDTO;
import app.fichajes.fichajes.services.PlannedTimetableService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/timetables")
public class PlannedTimetableController {

    private final PlannedTimetableService timetableService;

    @Autowired
    public PlannedTimetableController(PlannedTimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTimetable(@Valid @RequestBody CreateTimetableRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(timetableService.createTimetable(dto));
    }

    @GetMapping("/fetch")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(timetableService.getAll());
    }
}