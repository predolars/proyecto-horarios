package app.fichajes.fichajes.models.dtos.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PlannedTimetableResponseDTO {
    private Long id;
    private LocalDateTime plannedDateTimeStart;
    private LocalDateTime plannedDateTimeEnd;
    private AssignmentResponseDTO assignment;
    private AssignmentResponseDTO createByAssignment;
}