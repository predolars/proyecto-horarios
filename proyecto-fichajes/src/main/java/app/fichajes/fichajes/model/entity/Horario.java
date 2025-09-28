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
public class Horario {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_trabajador", nullable = false)
    private Trabajador trabajador;

}