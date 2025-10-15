package app.fichajes.fichajes.security;

import app.fichajes.fichajes.models.dtos.request.LoginRequestDTO;
import app.fichajes.fichajes.models.dtos.response.JwtAuthResponseDTO;
import app.fichajes.fichajes.models.dtos.response.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        System.out.println("Login");
        JwtAuthResponseDTO jwtAuthResponseDTO = authService.login(loginRequestDTO);

        return ResponseEntity.ok(jwtAuthResponseDTO);
    }

}
