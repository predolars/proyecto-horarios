package app.fichajes.fichajes.security;

import app.fichajes.fichajes.models.dtos.request.LoginRequestDTO;
import app.fichajes.fichajes.models.dtos.response.JwtAuthResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        log.info("Login request received");
        JwtAuthResponseDTO jwtAuthResponseDTO = authService.login(loginRequestDTO);

        return ResponseEntity.ok(jwtAuthResponseDTO);
    }

}
