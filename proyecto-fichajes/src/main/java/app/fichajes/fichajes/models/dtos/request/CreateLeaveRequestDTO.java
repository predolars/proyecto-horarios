package app.fichajes.fichajes.models.dtos.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CreateLeaveRequestDTO {
    @NotNull(message = "El ID de la asignación es obligatorio")
    private Long assignmentId;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @Future
    private LocalDateTime dateTimeStart;

    @NotNull(message = "La fecha de fin es obligatoria")
    @Future
    private LocalDateTime dateTimeEnd;

    @NotBlank(message = "El motivo de la ausencia es obligatorio")
    private String justifyReason;
}