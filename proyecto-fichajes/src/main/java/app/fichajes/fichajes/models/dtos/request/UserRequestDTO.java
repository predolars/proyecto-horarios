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
public class UserRequestDTO {

    @NotBlank(message = "The field cannot be null or empty")
    private String name;

    @NotBlank(message = "The field cannot be null or empty")
    private String surnames;

    private String dni;

    @Email(message = "The format is not correct (Example: address@gmail.com)")
    @NotBlank(message = "The field cannot be null or empty")
    private String email;

    private String phoneNumber;

    @NotBlank(message = "The field cannot be null or empty")
    @Length(min = 8, max = 20, message = "Minimum 8 and maximum 20 characters")
    private String password;
}
