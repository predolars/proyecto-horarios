package app.fichajes.fichajes.models.dtos.request;

import app.fichajes.fichajes.models.enums.TimeLogType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTimeLogRequestDTO {
    @NotNull(message = "El ID de la asignación es obligatorio")
    private Long assignmentId;

    @NotNull(message = "El tipo de fichaje (START o FINISH) es obligatorio")
    private TimeLogType timeLogType;
    
    private Double latitude;
    private Double longitude;
}