package app.fichajes.fichajes.models.entities;

import app.fichajes.fichajes.models.enums.TimeLogType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "time_log")
public class TimeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "datetime_timelog", nullable = false)
    @NotNull(message = "El campo no puede ser nulo o estar vacio")
    private LocalDateTime dateTimeTimelog;

    @Enumerated(EnumType.STRING)
    @Column(name = "timelog_type", nullable = false)
    @NotNull(message = "El campo no puede ser nulo o estar vacio")
    private TimeLogType timeLogType;

    @Column(name = "latitude_timelog")
    private Double latitudTimelog;

    @Column(name = "longitude_timelog")
    private Double longitudeTimelog;

    @Column(name = "summarized_hours")
    private Integer summarizedHours;

    // Relacion N,1 con tabla asignaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aproved_by_assignment_id")
    private Assignment aprovedByAssignment;

}