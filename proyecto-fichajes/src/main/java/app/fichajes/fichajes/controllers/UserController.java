package app.fichajes.fichajes.controllers;

import app.fichajes.fichajes.models.dtos.UserResponseDTO;
import app.fichajes.fichajes.models.entity.User;
import app.fichajes.fichajes.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {

        UserResponseDTO userResponse = userService.createUser(user);

        if (userResponse == null) {

            return ResponseEntity.badRequest().body("Puede haber algún campo duplicado");
        } else {
            return ResponseEntity.created(URI.create("http://localhost:8080/api/v1/users")).body(userResponse);
        }

    }

    @GetMapping("/get")
    public ResponseEntity<?> getAll() {

        return ResponseEntity.ok(userService.getAll());
    }

}
