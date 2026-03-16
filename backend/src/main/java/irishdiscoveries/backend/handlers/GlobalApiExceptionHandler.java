package irishdiscoveries.backend.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalApiExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        String message = e.getMessage() != null ? e.getMessage() : "Bad request";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        String message = e.getMessage() != null ? e.getMessage() : "Unexpected error";
        String lowerMessage = message.toLowerCase();

        if (lowerMessage.contains("not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        if (lowerMessage.contains("invalid username or password")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        String message = e.getMessage() != null ? e.getMessage() : "Unexpected error";
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }
}
