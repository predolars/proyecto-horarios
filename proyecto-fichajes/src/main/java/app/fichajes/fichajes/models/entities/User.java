package app.fichajes.fichajes.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El campo no puede ser nulo o estar vacio")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "El campo no puede ser nulo o estar vacio")
    @Column(nullable = false)
    private String surnames;

    @Column(unique = true)
    private String dni;

    @NotBlank(message = "El campo no puede ser nulo o estar vacio")
    @Email(message = "El email no es valido (estructura valida: ejemplo@gmail.com)")
    @Column(nullable = false, unique = true)
    private String email;

    private String phoneNumber;

    @NotBlank(message = "El campo no puede ser nulo o estar vacio")
    @Column(nullable = false)
    private String password;

    // Relacion 1,N con tabla assignments
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.LAZY)
    private List<Assignment> assignments = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return assignments.stream()
                .map(assignment -> new SimpleGrantedAuthority("ROLE_" + assignment.getRole().getRoleName().toUpperCase()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}