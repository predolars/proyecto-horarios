package app.fichajes.fichajes.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "leave_requests")
public class LeaveRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "datetime_start", nullable = false)
    private LocalDateTime dateTimeStart;

    @Column(name = "datetime_end", nullable = false)
    private LocalDateTime dateTimeEnd;

    @Column(name = "justify_reason", nullable = false)
    private String justifyReason;

    // Relacion N,1 con tabla asignaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "managed_by_assignment_id")
    private Assignment managedByAssignment;

}
