package app.fichajes.fichajes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExcepcionHandler {

    @ExceptionHandler(FieldDataAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponseDTO> handleEmailAlreadyExistsException(FieldDataAlreadyExistsException ex) {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO();

        exceptionResponseDTO.setStatus(HttpStatus.CONFLICT.value());
        exceptionResponseDTO.setError("Campo duplicado en conflicto");
        exceptionResponseDTO.setMessage(ex.getMessage());
        exceptionResponseDTO.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(exceptionResponseDTO.getStatus()).body(exceptionResponseDTO);
    }
}
