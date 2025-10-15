package app.fichajes.fichajes.models.dtos.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequestDTO {

    private String name;
    private String surnames;
    private String dni;

    @Email(message = "El formato no es el correcto (Ejemplo: direccion@gmail.com)")
    private String email;
    private String phoneNumber;

    @Length(min = 8, max = 20, message = "Minimo 8 y maximo 20 caracteres")
    private String password;
}
