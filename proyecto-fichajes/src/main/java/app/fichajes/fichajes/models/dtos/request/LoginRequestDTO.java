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

    @NotBlank(message = "El campo no puede ser nulo o estar vacio")
    String username;

    @NotBlank(message = "El campo no puede ser nulo o estar vacio")
    String password;
}
