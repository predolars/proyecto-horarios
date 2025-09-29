package app.fichajes.fichajes.model.entity;

import app.fichajes.fichajes.model.enums.EstadoConfirmacionFichaje;
import app.fichajes.fichajes.model.enums.TipoFichaje;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "fichaje_real")
public class FichajeReal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_hora_fichaje", nullable = false)
    private LocalDateTime fechaHoraEntradaReal;

    @Column(name = "tipo_fichaje", nullable = false)
    private TipoFichaje tipoFichaje;

    @Column(name = "latitud_fichaje")
    private Double latitudFichaje;

    @Column(name = "longitud_fichaje")
    private Double longitudFichaje;

    @Column(name = "estado_confimacion_fichaje")
    @Enumerated(EnumType.STRING)
    private EstadoConfirmacionFichaje estadoCorfirmacionFichaje = EstadoConfirmacionFichaje.PENDIENTE;

    @Column(name = "horas_computadas_final")
    private Integer horasComputadasFinal;

    // Relacion N,1 con tabla asignaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asignacion_id")
    private Asignacion asignacionId;

}