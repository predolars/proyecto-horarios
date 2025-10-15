package app.fichajes.fichajes.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity //para habilitar el soporte de seguridad basado en métodos. @PreAuthorize
public class SecurityConfig {

    final JwtAutenticationEntryPoint jwtAutenticationEntryPoint;
    final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConfig(JwtAutenticationEntryPoint jwtAutenticationEntryPoint, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAutenticationEntryPoint = jwtAutenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAutenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorize) -> {
                    authorize.requestMatchers("/**").permitAll();
//                    authorize.requestMatchers("/api/v1/companies/**").hasAnyRole("ROLE_JEFE", "ROLE_ENCARGADO");
//                    authorize.requestMatchers("/api/v1/users/**").hasAnyRole("ROLE_JEFE", "ROLE_ENCARGADO");
//                    authorize.requestMatchers("/api/v1/timetables/**").hasAnyRole("ROLE_JEFE", "ROLE_ENCARGADO");
//                    authorize.requestMatchers("/api/v1/assignments/**").hasAnyRole("ROLE_JEFE", "ROLE_ENCARGADO");
//                    authorize.requestMatchers("/api/v1/timelogs/**").authenticated();
//                    authorize.requestMatchers("/api/v1/roles/**").hasAnyRole("ROLE_JEFE", "ROLE_ENCARGADO");
//                    authorize.requestMatchers("/api/v1/leave-requests/fetch").hasAnyRole("ROLE_JEFE", "ROLE_ENCARGADO");
//                    authorize.requestMatchers("/api/v1/leave-requests/create").authenticated();

//                    authorize.anyRequest().authenticated();
                });//.httpBasic(Customizer.withDefaults());

        http.exceptionHandling(exception -> exception
                .authenticationEntryPoint(jwtAutenticationEntryPoint));

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
