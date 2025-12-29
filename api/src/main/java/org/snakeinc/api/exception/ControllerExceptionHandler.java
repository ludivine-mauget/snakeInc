package org.snakeinc.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<Object> handleException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        Map<String, String> errorsWithMessages = new HashMap<>();
        for (FieldError fieldError : fieldErrors) {
            errorsWithMessages.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErrorResponse bodyOfResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed for one or more fields.",
                errorsWithMessages
        );

        return ResponseEntity.badRequest().body(bodyOfResponse);
    }

    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<Object> handlePlayerNotFoundException(PlayerNotFoundException e) {
        ErrorResponse bodyOfResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                Map.of()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(bodyOfResponse);
    }

    @ExceptionHandler(InvalidSnakeTypeException.class)
    public ResponseEntity<Object> handleInvalidSnakeTypeException(InvalidSnakeTypeException e) {
        ErrorResponse bodyOfResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                Map.of()
        );
        return ResponseEntity.badRequest().body(bodyOfResponse);
    }

    public record ErrorResponse(int status, String message, Map<String, String> errors) { }
}