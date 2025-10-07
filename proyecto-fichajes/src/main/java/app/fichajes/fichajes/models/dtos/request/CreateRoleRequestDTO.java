package app.fichajes.fichajes.models.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateRoleRequestDTO {
    @NotBlank(message = "El nombre del rol no puede estar vacío")
    private String roleName;
}