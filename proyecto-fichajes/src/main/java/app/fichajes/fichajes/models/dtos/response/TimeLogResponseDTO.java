package app.fichajes.fichajes.models.dtos.response;

import app.fichajes.fichajes.models.enums.TimeLogType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TimeLogResponseDTO {
    private Long id;
    private LocalDateTime dateTimeTimelog;
    private TimeLogType timeLogType;
    private Double latitudTimelog;
    private Double longitudeTimelog;
    private Double summarizedHours;
    private Long assignmentId;
    private Long managedByAssignmentId;
}