package app.fichajes.fichajes.models.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TimeLogRequestDTO {
    @NotNull(message = "The assignment ID is mandatory")
    private Long assignmentId;
    
    private Double latitude;
    private Double longitude;
}