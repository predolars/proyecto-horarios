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
@Table(name = "leave_requests")
public class LeaveRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "datetime_start", nullable = false)
    @NotNull(message = "El campo no puede ser nulo o estar vacio")
    private LocalDateTime dateTimeStart;

    @Column(name = "datetime_end", nullable = false)
    @NotNull(message = "El campo no puede ser nulo o estar vacio")
    private LocalDateTime dateTimeEnd;

    @Column(name = "justify_reason", nullable = false)
    @NotBlank(message = "El campo no puede ser nulo o estar vacio")
    private String justifyReason;

    // Relacion N,1 con tabla asignaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "managed_by_assignment_id")
    private Assignment managedByAssignment;

}
