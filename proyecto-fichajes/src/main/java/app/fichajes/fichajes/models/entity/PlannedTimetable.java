package app.fichajes.fichajes.models.entity;

import jakarta.persistence.*;
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
    private LocalDateTime plannedDateTimeStart;

    @Column(name = "planned_datetime_end", nullable = false)
    private LocalDateTime plannedDateTimeEnd;

    // Relacion N,1 con tabla asignaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @ManyToOne
    @JoinColumn(name = "create_by_assignment_id")
    private Assignment createByAssignment;
}