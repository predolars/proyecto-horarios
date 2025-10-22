package app.fichajes.fichajes.models.dtos.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TimetableRequestDTO {
    @NotNull(message = "The assignment ID is mandatory")
    private Long assignmentId;

    @NotNull(message = "The start date and time are mandatory")
    @Future(message = "The start date must be after the current one")
    private LocalDateTime plannedDateTimeStart;

    @NotNull(message = "The end date and time are mandatory")
    @Future(message = "The end date must be after the current one")
    private LocalDateTime plannedDateTimeEnd;

    @NotNull(message = "The field cannot be empty")
    private Long createdByAssignmentId;
}