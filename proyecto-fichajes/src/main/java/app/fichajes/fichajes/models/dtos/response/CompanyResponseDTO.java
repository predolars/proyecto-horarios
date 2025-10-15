package app.fichajes.fichajes.models.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompanyResponseDTO {
    private Long id;
    private String cif;
    private String address;
    private Double latitude;
    private Double longitude;
    private String companyName;
}
