package app.fichajes.fichajes.services;

import app.fichajes.fichajes.exceptions.FieldDataAlreadyExistsException;
import app.fichajes.fichajes.exceptions.ResourceNotFoundException;
import app.fichajes.fichajes.models.dtos.request.AssignmentRequestDTO;
import app.fichajes.fichajes.models.dtos.response.AssignmentResponseDTO;
import app.fichajes.fichajes.models.entities.Assignment;
import app.fichajes.fichajes.models.entities.Company;
import app.fichajes.fichajes.models.entities.Role;
import app.fichajes.fichajes.models.entities.User;
import app.fichajes.fichajes.repositories.AssignmentRepository;
import app.fichajes.fichajes.repositories.CompanyRepository;
import app.fichajes.fichajes.repositories.RoleRepository;
import app.fichajes.fichajes.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AssignmentService(AssignmentRepository assignmentRepository, UserRepository userRepository,
                             CompanyRepository companyRepository, RoleRepository roleRepository,
                             ModelMapper modelMapper) {
        this.assignmentRepository = assignmentRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public AssignmentResponseDTO createAssignment(AssignmentRequestDTO dto) throws FieldDataAlreadyExistsException {
        User user = getUser(dto);
        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + dto.getCompanyId()));
        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + dto.getRoleId()));

        Assignment assignment = new Assignment();
        assignment.setUser(user);
        assignment.setCompany(company);
        assignment.setRole(role);

        if (assignmentRepository.existsByUserAndCompanyAndRole(user, company, role)) {
            throw new FieldDataAlreadyExistsException("This user is already assigned to the company: " + company.getCompanyName() + " with the role of: " + role.getRoleName());
        }

        Assignment savedAssignment = assignmentRepository.save(assignment);
        return modelMapper.map(savedAssignment, AssignmentResponseDTO.class);
    }

    @Transactional(readOnly = true)
    public List<AssignmentResponseDTO> getAll() {
        return assignmentRepository.findAll().stream()
                .map(assignment -> modelMapper.map(assignment, AssignmentResponseDTO.class))
                .toList();
    }

    @Transactional
    public void deleteAssignment(Long id) {
        if (!assignmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Assignment not found with id: " + id);
        }
        assignmentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<AssignmentResponseDTO> getMyAssignments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User currentUser = userRepository.findByEmail(userEmail).orElseThrow(() -> new ResourceNotFoundException("Assignment not found from token"));
        List<Assignment> assignments = assignmentRepository.findByUser(currentUser);

        return assignments.stream()
                .map(assignment -> modelMapper.map(assignment, AssignmentResponseDTO.class))
                .toList();
    }

    @Transactional
    public AssignmentResponseDTO updateAssignment(Long id, AssignmentRequestDTO dto) {
        // 1. Buscar la asignación existente
        Assignment assignmentDb = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + id));

        // 2. Cargar las nuevas entidades
        User user = getUser(dto);
        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + dto.getCompanyId()));
        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + dto.getRoleId()));

        // 3. Validar duplicados (solo si la combinación ha cambiado)
        if ((!assignmentDb.getUser().equals(user) ||
                !assignmentDb.getCompany().equals(company) ||
                !assignmentDb.getRole().equals(role)) && assignmentRepository.existsByUserAndCompanyAndRole(user, company, role)
        ) {
            throw new FieldDataAlreadyExistsException("This user is already assigned to this company with this role.");
        }


        // 4. Actualizar la entidad y dejar que @Transactional guarde
        assignmentDb.setUser(user);
        assignmentDb.setCompany(company);
        assignmentDb.setRole(role);

        return modelMapper.map(assignmentDb, AssignmentResponseDTO.class);
    }

    private User getUser(AssignmentRequestDTO dto) {
        return userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));
    }
}