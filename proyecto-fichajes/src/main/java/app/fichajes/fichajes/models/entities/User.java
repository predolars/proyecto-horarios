package app.fichajes.fichajes.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "The field cannot be null or empty")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "The field cannot be null or empty")
    @Column(nullable = false)
    private String surnames;

    @Column(unique = true)
    private String dni;

    @NotBlank(message = "The field cannot be null or empty")
    @Email(message = "The email is not valid (valid structure: example@gmail.com)")
    @Column(nullable = false, unique = true)
    private String email;

    private String phoneNumber;

    @NotBlank(message = "The field cannot be null or empty")
    @Column(nullable = false)
    private String password;

    // 1,N relationship with assignments table
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.LAZY)
    // Transient is used to avoid the serialization of UserDetails
    private transient List<Assignment> assignments = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return assignments.stream()
                .map(assignment -> new SimpleGrantedAuthority("ROLE_" + assignment.getRole().getRoleName().toUpperCase()))
                .toList();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }


}