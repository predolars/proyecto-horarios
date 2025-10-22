package app.fichajes.fichajes.controllers;

import app.fichajes.fichajes.models.dtos.request.TimetableRequestDTO;
import app.fichajes.fichajes.services.PlannedTimetableService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/timetables")
public class PlannedTimetableController {

    private final PlannedTimetableService timetableService;

    @Autowired
    public PlannedTimetableController(PlannedTimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @PostMapping
    public ResponseEntity<Object> createTimetables(@Valid @RequestBody List<TimetableRequestDTO> timetablesDTOs) {
        return ResponseEntity.status(HttpStatus.CREATED).body(timetableService.createTimetables(timetablesDTOs));
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(timetableService.getAll());
    }
}