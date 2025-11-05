package app.fichajes.fichajes.controllers;

import app.fichajes.fichajes.models.dtos.request.RoleRequestDTO;
import app.fichajes.fichajes.services.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('JEFE', 'ENCARGADO', 'ADMIN')")
    public ResponseEntity<Object> createRole(@Valid @RequestBody RoleRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.createRole(dto));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('JEFE', 'ENCARGADO', 'ADMIN')")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}