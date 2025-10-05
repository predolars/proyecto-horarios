package app.fichajes.fichajes.models.dtos;

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
public class UserRequestDTO {
    private String name;
    private String surnames;
    private String dni;

    @Email
    private String email;
    private String phoneNumber;
    private String password;
}
