package app.fichajes.fichajes.controllers;

import app.fichajes.fichajes.models.dtos.UserRequestDTO;
import app.fichajes.fichajes.models.dtos.UserResponseDTO;
import app.fichajes.fichajes.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Controller
@RequestMapping("/users")
public class UserController {

    final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequestDTO userRequest) {

        UserResponseDTO userResponse = userService.createUser(userRequest);
        return ResponseEntity.created(URI.create("http://localhost:8080/api/v1/users")).body(userResponse);
    }

    @GetMapping("/fetch")
    public ResponseEntity<?> getAll() {

        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/fetch/{id}")
    public ResponseEntity<?> fetchById(@PathVariable Long id) {

        UserResponseDTO userResponse = userService.findById(id);
        return ResponseEntity.ok(userResponse);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateByIp(@PathVariable Long id, @Valid @RequestBody UserRequestDTO userRequest) {

        UserResponseDTO userResponse = userService.updateUser(id, userRequest);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {

        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
