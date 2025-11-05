package app.fichajes.fichajes.models.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthResponseDTO {
    private String accessToken;
    private String tokenType;
    private UserResponseDTO userResponseDTO;

}
