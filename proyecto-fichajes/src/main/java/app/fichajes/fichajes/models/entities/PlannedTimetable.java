package app.fichajes.fichajes.models.entities;

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
@Table(name = "planned_timetables")
public class PlannedTimetable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "planned_datetime_start", nullable = false)
    @NotNull(message = "El campo no puede ser nulo o estar vacio")
    private LocalDateTime plannedDateTimeStart;

    @Column(name = "planned_datetime_end", nullable = false)
    @NotNull(message = "El campo no puede ser nulo o estar vacio")
    private LocalDateTime plannedDateTimeEnd;

    // Relacion N,1 con tabla asignaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @ManyToOne
    @JoinColumn(name = "create_by_assignment_id")
    private Assignment createByAssignment;
}