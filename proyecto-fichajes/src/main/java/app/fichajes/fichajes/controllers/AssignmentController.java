package app.fichajes.fichajes.controllers;

import app.fichajes.fichajes.models.dtos.request.AssignmentRequestDTO;
import app.fichajes.fichajes.models.dtos.response.AssignmentResponseDTO;
import app.fichajes.fichajes.services.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @Autowired
    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('JEFE', 'ENCARGADO', 'ADMIN')")
    public ResponseEntity<Object> createAssignment(@Valid @RequestBody AssignmentRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(assignmentService.createAssignment(dto));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('JEFE', 'ENCARGADO', 'ADMIN')")
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(assignmentService.getAll());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('JEFE', 'ENCARGADO', 'ADMIN')")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> getMyAssignments() {
        return ResponseEntity.ok(assignmentService.getMyAssignments());

    }

    /**
     * Actualiza una asignación (ej. cambiar el rol de un usuario en una empresa).
     * Requiere rol de gestión.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('JEFE', 'ENCARGADO', 'ADMIN')")
    public ResponseEntity<AssignmentResponseDTO> updateAssignment(@PathVariable Long id, @Valid @RequestBody AssignmentRequestDTO dto) {
        // Asumimos que el servicio 'updateAssignment' existe
        AssignmentResponseDTO response = assignmentService.updateAssignment(id, dto);
        return ResponseEntity.ok(response);
    }
}