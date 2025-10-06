package app.fichajes.fichajes.models.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateCompanyRequestDTO {

    @NotBlank(message = "El campo no puede ser nulo o estar vacio")
    private String cif;

    @NotBlank(message = "El campo no puede ser nulo o estar vacio")
    private String address;

    @NotNull(message = "El campo no puede ser nulo o estar vacio")
    private Double latitude;

    @NotNull(message = "El campo no puede ser nulo o estar vacio")
    private Double longitude;

    @NotBlank(message = "El campo no puede ser nulo o estar vacio")
    private String companyName;
}