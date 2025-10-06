package app.fichajes.fichajes.services;

import app.fichajes.fichajes.exceptions.FieldDataAlreadyExistsException;
import app.fichajes.fichajes.exceptions.ResourceNotFoundException;
import app.fichajes.fichajes.models.dtos.request.CreateCompanyRequestDTO;
import app.fichajes.fichajes.models.dtos.request.UpdateCompanyRequestDTO;
import app.fichajes.fichajes.models.dtos.response.CompanyResponseDTO;
import app.fichajes.fichajes.models.entities.Company;
import app.fichajes.fichajes.repositories.CompanyRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    final CompanyRepository companyRepository;
    final PasswordEncoder passwordEncoder;
    final ModelMapper modelMapper;

    @Autowired
    public CompanyService(CompanyRepository companyRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public CompanyResponseDTO createCompany(@Valid CreateCompanyRequestDTO companyRequest) {

        if (companyRepository.existsByCompanyName(companyRequest.getCompanyName()) || companyRepository.existsByCif(companyRequest.getCif())) {
            throw new FieldDataAlreadyExistsException("El cif o el nombre de la empresa ya existe en la base de datos");
        }

        Company companyToSave = modelMapper.map(companyRequest, Company.class);
        Company companySaved = companyRepository.save(companyToSave);

        return modelMapper.map(companySaved, CompanyResponseDTO.class);
    }

    public List<CompanyResponseDTO> getAll() {

        return companyRepository.findAll().stream().map(company -> modelMapper.map(company, CompanyResponseDTO.class)).toList();
    }

    public CompanyResponseDTO findById(Long id) {

        return modelMapper.map(companyRepository.findById(id), CompanyResponseDTO.class);
    }

    public CompanyResponseDTO updateCompany(Long id, @Valid UpdateCompanyRequestDTO companyRequest) {

        if (companyRequest.getCompanyName() != null && companyRepository.existsByCompanyName(companyRequest.getCompanyName())) {
            throw new FieldDataAlreadyExistsException("Ya existe una empresa con ese nombre: " + companyRequest.getCompanyName());
        }
        if (companyRequest.getCif() != null && companyRepository.existsByCif(companyRequest.getCif())) {
            throw new FieldDataAlreadyExistsException("Ya existe una empresa con ese CIF: " + companyRequest.getCif());
        }

        Company companyDb = companyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("La empresa con id: " + id + " no existe en la base de datos"));
        modelMapper.map(companyRequest, companyDb);

        Company updatedCompany = companyRepository.save(companyDb);

        return modelMapper.map(updatedCompany, CompanyResponseDTO.class);
    }

    public void deleteById(Long id) {

        if (!companyRepository.existsById(id)) {
            throw new ResourceNotFoundException("La empresa con id: " + id + " no existe en la base de datos");
        }
        companyRepository.deleteById(id);
    }
}
