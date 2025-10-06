package app.fichajes.fichajes.controllers;

import app.fichajes.fichajes.models.dtos.request.CreateCompanyRequestDTO;
import app.fichajes.fichajes.models.dtos.request.UpdateCompanyRequestDTO;
import app.fichajes.fichajes.models.dtos.response.CompanyResponseDTO;
import app.fichajes.fichajes.services.CompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCompany(@Valid @RequestBody CreateCompanyRequestDTO companyRequest) {

        CompanyResponseDTO companyResponse = companyService.createCompany(companyRequest);
        return ResponseEntity.created(URI.create("http://localhost:8080/api/v1/companies")).body(companyResponse);
    }

    @GetMapping("/fetch")
    public ResponseEntity<?> getAll() {

        return ResponseEntity.ok(companyService.getAll());
    }

    @GetMapping("/fetch/{id}")
    public ResponseEntity<?> fetchById(@PathVariable Long id) {

        CompanyResponseDTO companyResponse = companyService.findById(id);
        return ResponseEntity.ok(companyResponse);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateByIp(@PathVariable Long id, @Valid @RequestBody UpdateCompanyRequestDTO companyRequest) {

        CompanyResponseDTO companyResponse = companyService.updateCompany(id, companyRequest);
        return ResponseEntity.ok(companyResponse);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {

        companyService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
