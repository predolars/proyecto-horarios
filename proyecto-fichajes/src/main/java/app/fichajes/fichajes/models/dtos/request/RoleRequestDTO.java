package app.fichajes.fichajes.models.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleRequestDTO {
    @NotBlank(message = "The role name cannot be empty")
    private String roleName;
}