package app.fichajes.fichajes.model.entity;

import app.fichajes.fichajes.model.enums.EstadoConfirmacionHorario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "horarios")
public class HorarioPlanificado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_hora_entrada_planeada", nullable = false)
    private LocalDateTime fechaHoraEntradaPlaneada;

    @Column(name = "fecha_hora_salida_planeada", nullable = false)
    private LocalDateTime fechaHoraSalidaPlaneada;

    @Column(name = "estado_confirmacion_horario")
    @Enumerated(EnumType.STRING)
    private EstadoConfirmacionHorario estadoConfirmacionHorario = EstadoConfirmacionHorario.PLANIFICADO;

    // Relacion N,1 con tabla asignaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asignacion_id")
    private Asignacion asignacionId;

}