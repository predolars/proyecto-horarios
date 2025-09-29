package app.fichajes.fichajes.model.entity;

import app.fichajes.fichajes.model.enums.EstadoSuscripcion;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "empresas")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String direccion;

    private Double latitud;
    private Double longitud;

    @Column(name = "margen_permitido_metros")
    private Integer margenPermitidoMetros;

    @Column(name = "nombre_empresa", nullable = false, length = 100)
    private String nombreEmpresa;

    @Column(name = "cif", nullable = false, unique = true, length = 20)
    private String cif;

    @Column(name = "fecha_suscripcion")
    private LocalDate fechaSuscripcion;

    @Column(name = "estado_suscripcion", length = 50)
    private EstadoSuscripcion estadoSuscripcion = EstadoSuscripcion.ACTIVA;

    @Column(name = "registro_activo")
    private Boolean registroActivo = true;

    // Relacion 1,N con tabla asignaciones
    @OneToMany(mappedBy = "empresa_id")
    private List<Asignacion> asignaciones = new ArrayList<>();

}