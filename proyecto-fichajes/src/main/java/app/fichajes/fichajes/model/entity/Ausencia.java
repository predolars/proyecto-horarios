package app.fichajes.fichajes.model.entity;

import app.fichajes.fichajes.model.enums.EstadoConfirmacionAusencia;
import app.fichajes.fichajes.model.enums.TipoAusencia;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ausencias")
public class Ausencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_hora_inicio", nullable = false)
    private LocalDate fechaHoraInicio;

    @Column(name = "fecha_hora_fin", nullable = false)
    private LocalDate fechaHoraFin;

    @Enumerated(EnumType.STRING)
    private TipoAusencia tipoAusencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_confirmacion_ausencia")
    private EstadoConfirmacionAusencia estadoConfirmacionAusencia = EstadoConfirmacionAusencia.PENDIENTE;

    // Relacion N,1 con tabla asignaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asignacion_id")
    private Asignacion asignacionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gestionado_asignacion_id")
    private Asignacion gestionadoAsignacionId;

}
