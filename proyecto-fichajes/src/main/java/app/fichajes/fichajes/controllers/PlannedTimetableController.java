package app.fichajes.fichajes.controllers;

import app.fichajes.fichajes.models.dtos.request.TimetableRequestDTO;
import app.fichajes.fichajes.services.PlannedTimetableService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    @PreAuthorize("hasAnyRole('JEFE', 'ENCARGADO', 'ADMIN')")
    public ResponseEntity<Object> createTimetables(@Valid @RequestBody List<TimetableRequestDTO> timetablesDTOs) {
        return ResponseEntity.status(HttpStatus.CREATED).body(timetableService.createTimetables(timetablesDTOs));

    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(timetableService.getAll());

    }

    @GetMapping("/company/{managerAssignmentId}")
    @PreAuthorize("hasAnyRole('JEFE', 'ENCARGADO', 'ADMIN')")
    public ResponseEntity<Object> getTimetablesForCompany(@PathVariable Long managerAssignmentId) {
        return ResponseEntity.ok(timetableService.getTimetablesForCompany(managerAssignmentId));

    }

    @GetMapping("/my/{assignmentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> getMyTimetables(
            @PathVariable Long assignmentId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        return ResponseEntity.ok(timetableService.getTimetablesForAssignment(assignmentId, startDate, endDate));

    }

    /**
     * Actualiza un horario planificado existente.
     * Requiere rol de gestión.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('JEFE', 'ENCARGADO', 'ADMIN')")
    public ResponseEntity<Object> updateTimetable(@PathVariable Long id, @Valid @RequestBody TimetableRequestDTO timetableRequestDTO) {
        return ResponseEntity.ok(timetableService.updateTimetable(id, timetableRequestDTO));
    }

    /**
     * Elimina un horario planificado.
     * Requiere rol de gestión.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('JEFE', 'ENCARGADO', 'ADMIN')")
    public ResponseEntity<Void> deleteTimetable(@PathVariable Long id) {
        timetableService.deleteTimetable(id);
        return ResponseEntity.noContent().build();
    }
}