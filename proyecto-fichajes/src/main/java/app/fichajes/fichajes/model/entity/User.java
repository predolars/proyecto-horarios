package app.fichajes.fichajes.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 150)
    private String surnames;

    @Column(unique = true, length = 20)
    private String dni;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 9)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    // Relacion 1,N con tabla asignaciones
    @OneToMany(mappedBy = "user")
    private List<Assignment> assignments = new ArrayList<>();

}