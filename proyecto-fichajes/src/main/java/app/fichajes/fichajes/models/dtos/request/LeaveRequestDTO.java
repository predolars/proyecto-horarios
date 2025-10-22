package app.fichajes.fichajes.models.dtos.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LeaveRequestDTO {
    @NotNull(message = "The assignment ID is mandatory")
    private Long assignmentId;

    @NotNull(message = "The start date is mandatory")
    @Future(message = "The start date must be after the current one")
    private LocalDateTime dateTimeStart;

    @NotNull(message = "The end date is mandatory")
    @Future(message = "The end date must be after the current one")
    private LocalDateTime dateTimeEnd;

    @NotBlank(message = "The reason for the absence is mandatory")
    private String justifyReason;
}