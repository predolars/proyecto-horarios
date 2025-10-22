package app.fichajes.fichajes.models.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignmentRequestDTO {
    @NotNull(message = "The company ID is mandatory")
    private Long companyId;

    @NotNull(message = "The user ID is mandatory")
    private Long userId;

    @NotNull(message = "The role ID is mandatory")
    private Long roleId;
}