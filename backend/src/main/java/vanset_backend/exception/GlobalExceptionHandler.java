package vanset_backend.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
@ExceptionHandler(InvalidIdentifierException.class)
public ResponseEntity<Map<String, String>>
        handleInvalidIdentifier(
                InvalidIdentifierException exception) {

    Map<String, String> response =
            new HashMap<>();

    response.put(
            "error",
            "INVALID_IDENTIFIER"
    );

    response.put(
            "message",
            exception.getMessage()
    );

    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
}
    @ExceptionHandler(OtpRateLimitException.class)
    public ResponseEntity<Map<String, String>>
            handleOtpRateLimit(
                    OtpRateLimitException exception) {

        Map<String, String> response =
                new HashMap<>();

        response.put(
                "error",
                "OTP_RATE_LIMIT"
        );

        response.put(
                "message",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(response);
    }

    // Your existing exception handlers go below this.
}