package app.fichajes.fichajes.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull(message = "el nombre no puede estar vacio")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "los apellidos no pueden estar vacios")
    @Column(nullable = false)
    private String surnames;

    @Column(unique = true)
    private String dni;

    @NotNull(message = "el email no puede estar vacio")
    @Email(message = "el email no es valido (estructura valida: ejemplo@gmail.com)")
    @Column(nullable = false, unique = true)
    private String email;

    private String phoneNumber;

    @NotNull(message = "la password no puede estar vacia")
//    @Size(min = 8, max = 20, message = "la password debe tener entre 8 y 20 caracteres")
    @Column(nullable = false)
    private String password;

    // Relacion 1,N con tabla assignments
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
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