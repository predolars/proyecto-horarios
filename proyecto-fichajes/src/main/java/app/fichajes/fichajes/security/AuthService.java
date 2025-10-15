package app.fichajes.fichajes.security;

import app.fichajes.fichajes.models.dtos.request.LoginRequestDTO;
import app.fichajes.fichajes.models.dtos.response.JwtAuthResponseDTO;
import app.fichajes.fichajes.models.dtos.response.UserResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    final AuthenticationManager authenticationManager;
    final JWTUtil jwtUtil;
    final ModelMapper modelMapper;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JWTUtil jwtUtil, ModelMapper modelMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
    }


    /**
     * Realiza el login, genera el token y devuelve el DTO de respuesta completo.
     *
     * @param loginRequestDTO DTO con las credenciales del usuario.
     * @return Un DTO con el token de acceso y los datos del usuario logueado.
     */
    public JwtAuthResponseDTO login(LoginRequestDTO loginRequestDTO) {
        // 1. Autentica al usuario y obtiene el objeto Authentication
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsername(),
                        loginRequestDTO.getPassword()
                ));

        // 2. Establece la autenticación en el contexto de seguridad de Spring
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Genera el token JWT a partir del objeto Authentication
        String token = jwtUtil.generateToken(authentication);

        // 4. Obtiene el principal (que es tu entidad User) y lo mapea a un DTO de respuesta
        UserResponseDTO userResponseDTO = modelMapper.map(authentication.getPrincipal(), UserResponseDTO.class);

        // 5. Construye y devuelve el objeto de respuesta completo
        JwtAuthResponseDTO response = new JwtAuthResponseDTO();
        response.setAccessToken(token);
        response.setTokenType("jwt");
        response.setUserResponseDTO(userResponseDTO);

        return response;
    }

}
