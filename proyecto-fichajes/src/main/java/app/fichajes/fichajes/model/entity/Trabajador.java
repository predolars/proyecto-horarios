package app.fichajes.fichajes.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trabajadores")
public class Trabajador {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    @OneToMany(mappedBy = "trabajador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Horario> horarios;

    @OneToMany(mappedBy = "trabajador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FichajeReal> fichajes;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "trabajador_tienda",
            joinColumns = @JoinColumn(name = "id_trabajador"),
            inverseJoinColumns = @JoinColumn(name = "id_tienda")
    )
    private List<Tienda> tiendasAsignadas;

    @Column(name = "registro_activo")
    private Boolean registroActivo = true;

}