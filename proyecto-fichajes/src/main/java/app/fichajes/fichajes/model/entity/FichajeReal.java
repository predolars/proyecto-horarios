package app.fichajes.fichajes.model.entity;

import app.fichajes.fichajes.model.enums.EstadoConfirmacionFichaje;
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

    @Column(name = "fecha_hora_entrada_real", nullable = false)
    private LocalDateTime fechaHoraEntradaReal;

    @Column(name = "fecha_hora_salida_real")
    private LocalDateTime fechaHoraSalidaReal;

    @Column(name = "latitud_fichaje")
    private Double latitudFichaje;

    @Column(name = "longitud_fichaje")
    private Double longitudFichaje;

    @Column(name = "esta_en_zona")
    private Boolean estaEnZona;

    @Column(name = "justificacion_fuera_zona")
    private String justificacionFueraZona;

    @Column(name = "estado_confimacion_fichaje")
    @Enumerated(EnumType.STRING)
    private EstadoConfirmacionFichaje estadoCorfirmacionFichaje = EstadoConfirmacionFichaje.PENDIENTE;

    @Column(name = "horas_computadas_final")
    private Integer horasComputadasFinal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_trabajador", nullable = false)
    private Trabajador trabajador;

}