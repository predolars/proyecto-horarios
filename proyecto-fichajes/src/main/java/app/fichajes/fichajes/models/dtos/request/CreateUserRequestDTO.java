package app.fichajes.fichajes.models.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequestDTO {

    @NotBlank(message = "El campo no puede ser nulo o estar vacio")
    private String name;

    @NotBlank(message = "El campo no puede ser nulo o estar vacio")
    private String surnames;

    private String dni;

    @Email(message = "El formato no es el correcto (Ejemplo: example@gmail.com)")
    @NotBlank(message = "El campo no puede ser nulo o estar vacio")
    private String email;

    private String phoneNumber;

    @NotBlank(message = "El campo no puede ser nulo o estar vacio")
    @Length(min = 8, max = 20, message = "Minimo 8 y maximo 20 caracteres")
    private String password;
}
