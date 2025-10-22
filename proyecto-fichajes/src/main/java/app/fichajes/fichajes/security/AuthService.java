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
     * Performs login, generates the token, and returns the complete response DTO.
     *
     * @param loginRequestDTO DTO with the user's credentials.
     * @return A DTO with the access token and the logged-in user's data.
     */
    public JwtAuthResponseDTO login(LoginRequestDTO loginRequestDTO) {
        // 1. Authenticates the user and gets the Authentication object
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsername(),
                        loginRequestDTO.getPassword()
                ));

        // 2. Sets the authentication in Spring's security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Generates the JWT token from the Authentication object
        String token = jwtUtil.generateToken(authentication);

        // 4. Gets the principal (which is your User entity) and maps it to a response DTO
        UserResponseDTO userResponseDTO = modelMapper.map(authentication.getPrincipal(), UserResponseDTO.class);

        // 5. Builds and returns the complete response object
        JwtAuthResponseDTO response = new JwtAuthResponseDTO();
        response.setAccessToken(token);
        response.setTokenType("jwt");
        response.setUserResponseDTO(userResponseDTO);

        return response;
    }

}
