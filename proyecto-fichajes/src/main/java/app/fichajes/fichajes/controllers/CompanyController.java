package app.fichajes.fichajes.controllers;

import app.fichajes.fichajes.exceptions.ResourceNotFoundException;
import app.fichajes.fichajes.models.dtos.request.CompanyRequestDTO;
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
    public ResponseEntity<Object> createCompany(@Valid @RequestBody CompanyRequestDTO companyRequest) {

        CompanyResponseDTO companyResponse = companyService.createCompany(companyRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(companyResponse);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {

        return ResponseEntity.ok(companyService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> fetchById(@PathVariable Long id) {

        CompanyResponseDTO companyResponse = companyService.findById(id);
        return ResponseEntity.ok(companyResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateByIp(@PathVariable Long id, @Valid @RequestBody UpdateCompanyRequestDTO companyRequest) {

        CompanyResponseDTO companyResponse = companyService.updateCompany(id, companyRequest);
        return ResponseEntity.ok(companyResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {

        companyService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Advanced business logic
     **/

    @GetMapping("/{id}/assignments")
    public ResponseEntity<Object> getAsignmentesByCompany(@PathVariable Long id) throws ResourceNotFoundException {

        return ResponseEntity.status(HttpStatus.OK).body(companyService.getAssignmentsByCompany(id));
    }
}

