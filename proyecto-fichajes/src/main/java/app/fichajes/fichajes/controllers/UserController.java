package app.fichajes.fichajes.controllers;

import app.fichajes.fichajes.models.dtos.request.CreateUserRequestDTO;
import app.fichajes.fichajes.models.dtos.request.UpdateUserRequestDTO;
import app.fichajes.fichajes.models.dtos.response.UserResponseDTO;
import app.fichajes.fichajes.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequestDTO userRequest) {
        UserResponseDTO userResponse = userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);

    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(userService.getAll());

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> fetchById(@PathVariable Long id) {
        UserResponseDTO userResponse = userService.findById(id);
        return ResponseEntity.ok(userResponse);

    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateByIp(@PathVariable Long id, @Valid @RequestBody UpdateUserRequestDTO userRequest) {
        UserResponseDTO userResponse = userService.updateUser(id, userRequest);
        return ResponseEntity.ok(userResponse);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();

    }
}
