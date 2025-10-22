package app.fichajes.fichajes.controllers;

import app.fichajes.fichajes.models.dtos.request.UserRequestDTO;
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
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserRequestDTO userRequest) {
        UserResponseDTO userResponse = userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);

    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(userService.getAll());

    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> fetchById(@PathVariable Long id) {
        UserResponseDTO userResponse = userService.findById(id);
        return ResponseEntity.ok(userResponse);

    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateByIp(@PathVariable Long id, @Valid @RequestBody UpdateUserRequestDTO userRequest) {
        UserResponseDTO userResponse = userService.updateUser(id, userRequest);
        return ResponseEntity.ok(userResponse);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();

    }
}
