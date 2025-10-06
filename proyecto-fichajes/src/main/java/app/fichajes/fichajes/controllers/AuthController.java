package app.fichajes.fichajes.controllers;

import app.fichajes.fichajes.models.dtos.response.JwtAuthResponseDTO;
import app.fichajes.fichajes.models.dtos.request.LoginRequestDTO;
import app.fichajes.fichajes.security.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    final
    AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        System.out.println("Login");
        String token = authService.login(loginRequestDTO);
        JwtAuthResponseDTO jwtAuthResponseDTO = new JwtAuthResponseDTO();
        jwtAuthResponseDTO.setAccessToken(token);
        jwtAuthResponseDTO.setTokenType("jwt");
        return ResponseEntity.ok(jwtAuthResponseDTO);
    }
}
