package app.fichajes.fichajes.controllers;

import app.fichajes.fichajes.exceptions.ResourceNotFoundException;
import app.fichajes.fichajes.models.dtos.request.CreateCompanyRequestDTO;
import app.fichajes.fichajes.models.dtos.request.UpdateCompanyRequestDTO;
import app.fichajes.fichajes.models.dtos.response.CompanyResponseDTO;
import app.fichajes.fichajes.services.CompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    public ResponseEntity<?> createCompany(@Valid @RequestBody CreateCompanyRequestDTO companyRequest) {

        CompanyResponseDTO companyResponse = companyService.createCompany(companyRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(companyResponse);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {

        return ResponseEntity.ok(companyService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> fetchById(@PathVariable Long id) {

        CompanyResponseDTO companyResponse = companyService.findById(id);
        return ResponseEntity.ok(companyResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateByIp(@PathVariable Long id, @Valid @RequestBody UpdateCompanyRequestDTO companyRequest) {

        CompanyResponseDTO companyResponse = companyService.updateCompany(id, companyRequest);
        return ResponseEntity.ok(companyResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {

        companyService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Lógica de negocio avanzada
     **/

    @GetMapping("/{id}/assignments")
    public ResponseEntity<?> getAsignmentesByCompany(@PathVariable Long id) throws ResourceNotFoundException {

        return ResponseEntity.status(HttpStatus.OK).body(companyService.getAssignmentsByCompany(id));
    }
}

