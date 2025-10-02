package app.fichajes.fichajes.controllers;

import app.fichajes.fichajes.model.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        return ResponseEntity.ok(user);
    }

}
