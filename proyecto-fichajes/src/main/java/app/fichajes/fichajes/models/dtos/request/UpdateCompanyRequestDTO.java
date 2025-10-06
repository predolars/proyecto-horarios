package app.fichajes.fichajes.models.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateCompanyRequestDTO {

    private String cif;
    private String address;
    private Double latitude;
    private Double longitude;
    private String companyName;
}