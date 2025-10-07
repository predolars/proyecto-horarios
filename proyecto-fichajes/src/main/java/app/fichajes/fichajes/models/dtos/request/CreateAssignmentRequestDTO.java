package app.fichajes.fichajes.models.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateAssignmentRequestDTO {
    @NotNull(message = "El ID de la empresa es obligatorio")
    private Long companyId;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;

    @NotNull(message = "El ID del rol es obligatorio")
    private Long roleId;
}