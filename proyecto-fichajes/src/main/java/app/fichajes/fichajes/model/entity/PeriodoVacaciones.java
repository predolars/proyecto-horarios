package app.fichajes.fichajes.model.entity;

import app.fichajes.fichajes.model.enums.EstadoConfirmacionVacaciones;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "periodo_vacaciones")
public class PeriodoVacaciones {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_confirmacion_vacaciones")
    private EstadoConfirmacionVacaciones estadoConfirmacionVacaciones = EstadoConfirmacionVacaciones.PENDIENTE;

}
