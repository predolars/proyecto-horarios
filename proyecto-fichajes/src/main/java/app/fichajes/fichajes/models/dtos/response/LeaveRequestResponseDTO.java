package app.fichajes.fichajes.models.dtos.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LeaveRequestResponseDTO {
    private Long id;
    private LocalDateTime dateTimeStart;
    private LocalDateTime dateTimeEnd;
    private String justifyReason;
    private AssignmentResponseDTO assignment;
    private AssignmentResponseDTO managedByAssignment;
}