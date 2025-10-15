package app.fichajes.fichajes.services;

import app.fichajes.fichajes.exceptions.FieldDataAlreadyExistsException;
import app.fichajes.fichajes.exceptions.ResourceNotFoundException;
import app.fichajes.fichajes.models.dtos.request.CreateUserRequestDTO;
import app.fichajes.fichajes.models.dtos.request.UpdateUserRequestDTO;
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

    public UserResponseDTO createUser(CreateUserRequestDTO userRequest) {

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new FieldDataAlreadyExistsException("El email ya existe en la base de datos");
        }

        userRequest.setDni(userRequest.getDni().toUpperCase());
        userRequest.setEmail(userRequest.getEmail().toLowerCase());

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

    public UserResponseDTO findById(Long id) {
        User userDb = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El usuario no se ha encontrado"));

        return modelMapper.map(userDb, UserResponseDTO.class);

    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UpdateUserRequestDTO userRequest) {

        User userDb = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El usuario no existe en la base de datos"));

        // Comprobación especial para el email si se quiere cambiar
        if (userRequest.getEmail() != null && userRequest.getEmail().equalsIgnoreCase(userDb.getEmail())) {
            throw new FieldDataAlreadyExistsException("El email ya existe en la base de datos");
        }

//        userRequest.setEmail(userRequest.getEmail().toLowerCase());
//
//        if (userRequest.getDni() != null && !userRequest.getDni().equalsIgnoreCase(userDb.getDni())) {
//            throw new FieldDataAlreadyExistsException("El email ya existe en la base de datos");
//        }
//
//        userRequest.setDni(userRequest.getDni().toUpperCase());

        modelMapper.map(userRequest, userDb);
//        User updatedUser = userRepository.save(userDb);
        return modelMapper.map(userDb, UserResponseDTO.class);

    }

    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("El usuario con id " + id + " no existe en la base de datos");
        }

        userRepository.deleteById(id);
    }
}
