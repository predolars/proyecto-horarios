package app.fichajes.fichajes.controllers;

import app.fichajes.fichajes.models.dtos.request.CreateAssignmentRequestDTO;
import app.fichajes.fichajes.services.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @Autowired
    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAssignment(@Valid @RequestBody CreateAssignmentRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(assignmentService.createAssignment(dto));
    }

    @GetMapping("/fetch")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(assignmentService.getAll());
    }

    @DeleteMapping("/fetch/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }
}