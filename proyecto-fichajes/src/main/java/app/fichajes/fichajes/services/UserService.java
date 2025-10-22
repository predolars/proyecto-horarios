package app.fichajes.fichajes.services;

import app.fichajes.fichajes.exceptions.FieldDataAlreadyExistsException;
import app.fichajes.fichajes.exceptions.ResourceNotFoundException;
import app.fichajes.fichajes.models.dtos.request.UpdateUserRequestDTO;
import app.fichajes.fichajes.models.dtos.request.UserRequestDTO;
import app.fichajes.fichajes.models.dtos.response.UserResponseDTO;
import app.fichajes.fichajes.models.entities.User;
import app.fichajes.fichajes.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userRequest) {

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new FieldDataAlreadyExistsException("The email already exists in the database");
        }

        userRequest.setDni(userRequest.getDni().toUpperCase().trim());
        userRequest.setEmail(userRequest.getEmail().toLowerCase().trim());

        User user = modelMapper.map(userRequest, User.class);

        String hashedPassword = passwordEncoder.encode(userRequest.getPassword());
        user.setPassword(hashedPassword);

        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserResponseDTO.class);

    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAll() {
        return userRepository.findAll().stream().map(user -> modelMapper.map(user, UserResponseDTO.class)).toList();

    }

    @Transactional(readOnly = true)
    public UserResponseDTO findById(Long id) {
        User userDb = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return modelMapper.map(userDb, UserResponseDTO.class);

    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UpdateUserRequestDTO userRequest) {

        User userDb = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The user does not exist in the database"));

        // 1. Validate and update the Email ONLY if a new and different one is provided
        if (userRequest.getEmail() != null && !userRequest.getEmail().equalsIgnoreCase(userDb.getEmail())) {
            if (userRepository.existsByEmail(userRequest.getEmail())) {
                throw new FieldDataAlreadyExistsException("The email '" + userRequest.getEmail() + "' is already in use.");
            }
            userRequest.setEmail(userRequest.getEmail().toLowerCase().trim());
        }

        // 2. Validate and update the DNI ONLY if a new and different one is provided
        if (userRequest.getDni() != null && !userRequest.getDni().equalsIgnoreCase(userDb.getDni())) {
            if (userRepository.existsByDni(userRequest.getDni())) {
                throw new FieldDataAlreadyExistsException("The DNI '" + userRequest.getDni() + "' is already in use.");
            }
            userRequest.setDni(userRequest.getDni().toLowerCase().trim());

        }

        // 3. If a new password is provided, we encode it in the DTO
        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }

        // We map all changes from the DTO to the entity at once.
        modelMapper.map(userRequest, userDb);

        // It is not necessary to call userRepository.save().
        // The transaction will take care of persisting the changes in userDb.
        return modelMapper.map(userDb, UserResponseDTO.class);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with id " + id + " does not exist in the database");
        }

        userRepository.deleteById(id);
    }
}
