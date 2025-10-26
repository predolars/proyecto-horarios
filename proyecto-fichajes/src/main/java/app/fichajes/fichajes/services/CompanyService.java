package app.fichajes.fichajes.services;

import app.fichajes.fichajes.exceptions.FieldDataAlreadyExistsException;
import app.fichajes.fichajes.exceptions.ResourceNotFoundException;
import app.fichajes.fichajes.models.dtos.request.CompanyRequestDTO;
import app.fichajes.fichajes.models.dtos.request.UpdateCompanyRequestDTO;
import app.fichajes.fichajes.models.dtos.response.AssignmentResponseDTO;
import app.fichajes.fichajes.models.dtos.response.CompanyResponseDTO;
import app.fichajes.fichajes.models.entities.Company;
import app.fichajes.fichajes.repositories.CompanyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public CompanyResponseDTO createCompany(CompanyRequestDTO companyRequestDTO) {
        if (companyRepository.existsByCompanyName(companyRequestDTO.getCompanyName())) {
            throw new FieldDataAlreadyExistsException("The company name already exists in the database: " + companyRequestDTO.getCompanyName());
        }
        if (companyRepository.existsByCif(companyRequestDTO.getCif())) {
            throw new FieldDataAlreadyExistsException("The CIF already exists in the database: " + companyRequestDTO.getCif());
        }

        companyRequestDTO.setCif(companyRequestDTO.getCif().toUpperCase());

        Company companyToSave = modelMapper.map(companyRequestDTO, Company.class);
        Company companySaved = companyRepository.save(companyToSave);

        return modelMapper.map(companySaved, CompanyResponseDTO.class);
    }

    @Transactional(readOnly = true)
    public List<CompanyResponseDTO> getAll() {

        return companyRepository.findAll().stream().map(company -> modelMapper.map(company, CompanyResponseDTO.class)).toList();
    }

    public CompanyResponseDTO findById(Long id) {

        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company with id: " + id + " does not exist"));
        return modelMapper.map(company, CompanyResponseDTO.class);
    }

    @Transactional
    public CompanyResponseDTO updateCompany(Long id, UpdateCompanyRequestDTO companyRequest) {

        Company companyDb = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company with id: " + id + " does not exist"));

        // With a single query, we validate if the new data would conflict with ANOTHER company
        if (companyRequest.getCompanyName() != null || companyRequest.getCif() != null) {

            // We normalize the CIF if it is not null BEFORE validation
            if (companyRequest.getCif() != null) {
                companyRequest.setCif(companyRequest.getCif().toUpperCase());
            }

            companyRepository.findByCompanyNameOrCifAndIdNot(
                    companyRequest.getCompanyName(),
                    companyRequest.getCif(),
                    id
            ).ifPresent(existingCompany -> {
                // If we find a company, we throw the exception
                if (existingCompany.getCompanyName().equalsIgnoreCase(companyRequest.getCompanyName())) {
                    throw new FieldDataAlreadyExistsException("Another company with the name already exists: " + companyRequest.getCompanyName());
                }
                if (existingCompany.getCif().equalsIgnoreCase(companyRequest.getCif())) {
                    throw new FieldDataAlreadyExistsException("Another company with the CIF already exists: " + companyRequest.getCif());
                }
            });

        }

        modelMapper.map(companyRequest, companyDb);
        return modelMapper.map(companyDb, CompanyResponseDTO.class);
    }

    @Transactional
    public void deleteById(Long id) {

        if (!companyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Company with id: " + id + " does not exist in the database");
        }
        companyRepository.deleteById(id);
    }

    /** Method to return the list of assignments a company has **/
    public List<AssignmentResponseDTO> getAssignmentsByCompany(Long id) {

        Company company = companyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("Company with id %s does not exist", id)));

        return company.getAssignments().stream().map(assignment -> modelMapper.map(assignment, AssignmentResponseDTO.class)).toList();
    }
}
