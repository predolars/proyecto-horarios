package app.fichajes.fichajes.models.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {

    @NotBlank(message = "The field cannot be null or empty")
    String username;

    @NotBlank(message = "The field cannot be null or empty")
    String password;
}
