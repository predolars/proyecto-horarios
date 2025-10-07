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
    @Future(message = "La fecha de inicio debe ser en el futuro")
    private LocalDateTime plannedDateTimeStart;

    @NotNull(message = "La fecha y hora de fin son obligatorias")
    @Future(message = "La fecha de fin debe ser en el futuro")
    private LocalDateTime plannedDateTimeEnd;

    // Podrías añadir un campo para el ID del manager que crea el horario
    private Long createdByAssignmentId;
}