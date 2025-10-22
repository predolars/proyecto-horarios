package app.fichajes.fichajes.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExcepcionHandler {

    @ExceptionHandler(FieldDataAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponseDTO> handleFieldDataAlreadyExistsException(FieldDataAlreadyExistsException ex) {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO();

        exceptionResponseDTO.setStatus(HttpStatus.CONFLICT.value());
        exceptionResponseDTO.setError("Field data conflict");
        exceptionResponseDTO.setMessage(ex.getMessage());

        exceptionResponseDTO.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(exceptionResponseDTO.getStatus()).body(exceptionResponseDTO);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO();

        exceptionResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
        exceptionResponseDTO.setError("Resource not found");
        exceptionResponseDTO.setMessage(ex.getMessage());
        exceptionResponseDTO.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(exceptionResponseDTO.getStatus()).body(exceptionResponseDTO);
    }

    /**
     * This method is activated when a validation on a DTO annotated with @Valid fails.
     * It collects all field error messages and returns them in a clear JSON.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return getObjectResponseEntity(errors);
    }

    /**
     * In case the method above does not capture the exception correctly, this one will be activated when a validation on a DTO annotated with @Valid fails.
     * It collects all field error messages and returns them in a clear JSON.
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<Object> handlerMethodValidationException(HandlerMethodValidationException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return getObjectResponseEntity(errors);
    }

    private ResponseEntity<Object> getObjectResponseEntity(Map<String, String> errors) {
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO();

        exceptionResponseDTO.setError("Validation error");
        exceptionResponseDTO.setMessage("One or more fields have conflicted with data validation");
        exceptionResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
        exceptionResponseDTO.setTimestamp(LocalDateTime.now());
        exceptionResponseDTO.setFieldErrors(errors);

        return ResponseEntity.status(exceptionResponseDTO.getStatus()).body(exceptionResponseDTO);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {

        ExceptionResponseDTO response = new ExceptionResponseDTO(
                HttpStatus.CONFLICT.value(),
                "Data Conflict",
                ex.getMessage(),
//                "The data provided conflicts with existing records. It is possible that the DNI, CIF or email are already in use.",
                LocalDateTime.now(),
                null
        );

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponseDTO> handleAuthenticationException(AuthenticationException ex) {

        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO(
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                ex.getCause().getMessage(),
//                "Incorrect credentials, please try again.",
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.status(exceptionResponseDTO.getStatus()).body(exceptionResponseDTO);
    }

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<ExceptionResponseDTO> handleGenericException(GenericException e) {

        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "EXCEPTION",
                e.getMessage(),
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.status(exceptionResponseDTO.getStatus()).body(exceptionResponseDTO);
    }
}
