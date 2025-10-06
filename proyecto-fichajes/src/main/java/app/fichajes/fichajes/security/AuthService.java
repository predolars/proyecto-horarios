package app.fichajes.fichajes.security;

import app.fichajes.fichajes.models.dtos.request.LoginRequestDTO;
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

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public String login(LoginRequestDTO loginRequestDTO) {
        System.out.println("AuthService Login");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsername(),
                        loginRequestDTO.getPassword()
                ));
        System.out.println(authentication.toString());
        System.out.println();
        authentication.getAuthorities().forEach(System.out::println);
        System.out.println();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtUtil.generateToken(authentication);
    }

}
