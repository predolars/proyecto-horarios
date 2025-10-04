package app.fichajes.fichajes.services;

import app.fichajes.fichajes.models.dtos.UserResponseDTO;
import app.fichajes.fichajes.models.entity.User;
import app.fichajes.fichajes.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO createUser(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está en uso");
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        User savedUser = userRepository.save(user);

        return mapUserToResponseDTO(savedUser);
    }

    public List<UserResponseDTO> getAll() {
        return userRepository.findAll().stream().map(this::mapUserToResponseDTO).toList();
    }

    private UserResponseDTO mapUserToResponseDTO(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setName(user.getName());
        userResponseDTO.setSurnames(user.getSurnames());
        userResponseDTO.setDni(user.getDni());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setPhoneNumber(user.getPhoneNumber());

        return userResponseDTO;
    }
}
