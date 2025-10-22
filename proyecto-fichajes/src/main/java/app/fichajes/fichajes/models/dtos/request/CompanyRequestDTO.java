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
public class CompanyRequestDTO {

    @NotBlank(message = "The field cannot be null or empty")
    private String cif;

    @NotBlank(message = "The field cannot be null or empty")
    private String address;

    @NotNull(message = "The field cannot be null or empty")
    private Double latitude;

    @NotNull(message = "The field cannot be null or empty")
    private Double longitude;

    @NotBlank(message = "The field cannot be null or empty")
    private String companyName;
}