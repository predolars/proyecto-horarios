package app.fichajes.fichajes.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExcepcionHandler {

    @ExceptionHandler(FieldDataAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponseDTO> handleFieldDataAlreadyExistsException(FieldDataAlreadyExistsException ex) {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO();

        exceptionResponseDTO.setStatus(HttpStatus.CONFLICT.value());
        exceptionResponseDTO.setError("Campo duplicado en conflicto");
        exceptionResponseDTO.setMessage(ex.getMessage());
        exceptionResponseDTO.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(exceptionResponseDTO.getStatus()).body(exceptionResponseDTO);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO();

        exceptionResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
        exceptionResponseDTO.setError("Recurso no econtrado");
        exceptionResponseDTO.setMessage(ex.getMessage());
        exceptionResponseDTO.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(exceptionResponseDTO.getStatus()).body(exceptionResponseDTO);
    }

    /**
     * Este método se activa cuando falla una validación en un DTO anotado con @Valid.
     * Recoge todos los mensajes de error de los campos y los devuelve en un JSON claro.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO();

        exceptionResponseDTO.setError("Error de validación");
        exceptionResponseDTO.setMessage("Uno o varios campos han entrado en conflicto con la validación de datos");
        exceptionResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
        exceptionResponseDTO.setTimestamp(LocalDateTime.now());
        exceptionResponseDTO.setFieldErrors(errors);

        return ResponseEntity.status(exceptionResponseDTO.getStatus()).body(exceptionResponseDTO);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {

        ExceptionResponseDTO response = new ExceptionResponseDTO(
                HttpStatus.CONFLICT.value(),
                "Conflicto de Datos",
                "Los datos proporcionados entran en conflicto con registros existentes. Es posible que el DNI, CIF o email ya estén en uso.",
                LocalDateTime.now(),
                null
        );

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}
