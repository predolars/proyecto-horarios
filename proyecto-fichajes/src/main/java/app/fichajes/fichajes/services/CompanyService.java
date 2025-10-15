package app.fichajes.fichajes.services;

import app.fichajes.fichajes.exceptions.FieldDataAlreadyExistsException;
import app.fichajes.fichajes.exceptions.ResourceNotFoundException;
import app.fichajes.fichajes.models.dtos.request.CreateCompanyRequestDTO;
import app.fichajes.fichajes.models.dtos.request.UpdateCompanyRequestDTO;
import app.fichajes.fichajes.models.dtos.response.AssignmentResponseDTO;
import app.fichajes.fichajes.models.dtos.response.CompanyResponseDTO;
import app.fichajes.fichajes.models.entities.Company;
import app.fichajes.fichajes.repositories.CompanyRepository;
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

    public CompanyResponseDTO createCompany(CreateCompanyRequestDTO companyRequestDTO) {
        if (companyRepository.existsByCompanyName(companyRequestDTO.getCompanyName())) {
            throw new FieldDataAlreadyExistsException("El nombre de la empresa ya existe en la base de datos: " + companyRequestDTO.getCompanyName());
        }
        if (companyRepository.existsByCif(companyRequestDTO.getCif())) {
            throw new FieldDataAlreadyExistsException("El CIF ya existe en la base de datos: " + companyRequestDTO.getCif());
        }

        companyRequestDTO.setCif(companyRequestDTO.getCif().toUpperCase());

        Company companyToSave = modelMapper.map(companyRequestDTO, Company.class);
        Company companySaved = companyRepository.save(companyToSave);

        return modelMapper.map(companySaved, CompanyResponseDTO.class);
    }

    public List<CompanyResponseDTO> getAll() {

        return companyRepository.findAll().stream().map(company -> modelMapper.map(company, CompanyResponseDTO.class)).toList();
    }

    public CompanyResponseDTO findById(Long id) {

        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La empresa con id: " + id + " no existe"));
        return modelMapper.map(company, CompanyResponseDTO.class);
    }

    public CompanyResponseDTO updateCompany(Long id, UpdateCompanyRequestDTO companyRequest) {

        Company companyDb = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La empresa con id: " + id + " no existe"));

        // Con una sola consulta, validamos si los nuevos datos entrarían en conflicto con OTRA empresa
        if (companyRequest.getCompanyName() != null || companyRequest.getCif() != null) {

            // Normalizamos el CIF si no es nulo ANTES de la validación
            if (companyRequest.getCif() != null) {
                companyRequest.setCif(companyRequest.getCif().toUpperCase());
            }

            companyRepository.findByCompanyNameOrCifAndIdNot(
                    companyRequest.getCompanyName(),
                    companyRequest.getCif(),
                    id
            ).ifPresent(existingCompany -> {
                // Si encontramos una empresa, lanzamos la excepción
                if (existingCompany.getCompanyName().equalsIgnoreCase(companyRequest.getCompanyName())) {
                    throw new FieldDataAlreadyExistsException("Ya existe otra empresa con el nombre: " + companyRequest.getCompanyName());
                }
                if (existingCompany.getCif().equalsIgnoreCase(companyRequest.getCif())) {
                    throw new FieldDataAlreadyExistsException("Ya existe otra empresa con el CIF: " + companyRequest.getCif());
                }
            });

        }

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

    /** Metodo para devolver la lista de asignaciones que tiene una compañia **/
    public List<AssignmentResponseDTO> getAssignmentsByCompany(Long id) {

        Company company = companyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("La empresa con id%s no existe", id)));

        return company.getAssignments().stream().map(assignment -> modelMapper.map(assignment, AssignmentResponseDTO.class)).toList();
    }
}
