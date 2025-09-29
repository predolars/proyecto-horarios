package app.fichajes.fichajes.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 150)
    private String apellidos;

    @Column(unique = true, length = 20)
    private String dni;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 9)
    private String telefono;

    @Column(nullable = false)
    private String password;

    @Column(name = "registro_activo")
    private Boolean registroActivo = true;

    // Relacion 1,N con tabla asignaciones
    @OneToMany(mappedBy = "usuarioId")
    private List<Asignacion> asignaciones = new ArrayList<>();

}