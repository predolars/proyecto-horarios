package app.fichajes.fichajes.models.dtos.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CreateTimetableRequestDTO {
    @NotNull(message = "El ID de la asignación es obligatorio")
    private Long assignmentId;

    @NotNull(message = "La fecha y hora de inicio son obligatorias")
    @Future(message = "La fecha de inicio debe ser posterior a la actual")
    private LocalDateTime plannedDateTimeStart;

    @NotNull(message = "La fecha y hora de fin son obligatorias")
    @Future(message = "La fecha de inicio debe ser posterior a la actual")
    private LocalDateTime plannedDateTimeEnd;

    @NotNull(message = "El campo no puede estar vacío")
    private Long createdByAssignmentId;
}