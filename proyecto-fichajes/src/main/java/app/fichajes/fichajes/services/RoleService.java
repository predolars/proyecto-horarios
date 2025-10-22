package app.fichajes.fichajes.services;

import app.fichajes.fichajes.exceptions.FieldDataAlreadyExistsException;
import app.fichajes.fichajes.exceptions.ResourceNotFoundException;
import app.fichajes.fichajes.models.dtos.request.RoleRequestDTO;
import app.fichajes.fichajes.models.dtos.response.RoleResponseDTO;
import app.fichajes.fichajes.models.entities.Role;
import app.fichajes.fichajes.repositories.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RoleService(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public RoleResponseDTO createRole(RoleRequestDTO roleRequestDTO) {
        if (roleRepository.existsByRoleName(roleRequestDTO.getRoleName())) {
            throw new FieldDataAlreadyExistsException("This role already exists in the database: " + roleRequestDTO.getRoleName());
        }

        roleRequestDTO.setRoleName(roleRequestDTO.getRoleName().toUpperCase());

        Role role = modelMapper.map(roleRequestDTO, Role.class);
        Role savedRole = roleRepository.save(role);
        return modelMapper.map(savedRole, RoleResponseDTO.class);

    }

    @Transactional(readOnly = true)
    public List<RoleResponseDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(role -> modelMapper.map(role, RoleResponseDTO.class))
                .toList();

    }

    @Transactional
    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Role not found with id: " + id);
        }
        roleRepository.deleteById(id);

    }
}