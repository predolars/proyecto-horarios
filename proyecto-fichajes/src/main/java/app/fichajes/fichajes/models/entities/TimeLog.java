package app.fichajes.fichajes.models.entities;

import app.fichajes.fichajes.models.enums.TimeLogType;
import jakarta.persistence.*;
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
    @NotNull(message = "The field cannot be null or empty")
    private LocalDateTime dateTimeTimelog;

    @Enumerated(EnumType.STRING)
    @Column(name = "timelog_type", nullable = false)
    @NotNull(message = "The field cannot be null or empty")
    private TimeLogType timeLogType;

    @Column(name = "latitude_timelog")
    private Double latitudTimelog;

    @Column(name = "longitude_timelog")
    private Double longitudeTimelog;

    @Column(name = "summarized_hours")
    private Double summarizedHours;

    // N,1 relationship with assignments table
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by_assignment_id")
    private Assignment approvedByAssignment;

}