package app.fichajes.fichajes.controllers;

import app.fichajes.fichajes.models.dtos.JwtAuthResponse;
import app.fichajes.fichajes.models.dtos.LoginDTO;
import app.fichajes.fichajes.security.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO){
        System.out.println("Login");
        String token = authService.login(loginDTO);
        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(token);
        jwtAuthResponse.setTokenType("jwt");
        return ResponseEntity.ok(jwtAuthResponse);
    }
}
