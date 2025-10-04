package app.fichajes.fichajes.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponseDTO {
    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;
}
