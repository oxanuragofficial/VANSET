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
@ExceptionHandler(InvalidOtpException.class)
public ResponseEntity<Map<String, String>>
        handleInvalidOtp(
                InvalidOtpException exception) {

    Map<String, String> response =
            new HashMap<>();

    response.put(
            "error",
            "INVALID_OTP"
    );

    response.put(
            "message",
            exception.getMessage()
    );

    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
}
@ExceptionHandler(OtpAttemptsExceededException.class)
public ResponseEntity<Map<String, String>>
        handleOtpAttemptsExceeded(
                OtpAttemptsExceededException exception) {

    Map<String, String> response =
            new HashMap<>();

    response.put(
            "error",
            "OTP_ATTEMPTS_EXCEEDED"
    );

    response.put(
            "message",
            exception.getMessage()
    );

    return ResponseEntity
            .status(HttpStatus.TOO_MANY_REQUESTS)
            .body(response);
}
@ExceptionHandler(OtpExpiredException.class)
public ResponseEntity<Map<String, String>>
        handleOtpExpired(
                OtpExpiredException exception) {

    Map<String, String> response =
            new HashMap<>();

    response.put(
            "error",
            "OTP_EXPIRED"
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
    @ExceptionHandler(UnauthorizedResourceAccessException.class)
public ResponseEntity<Map<String, String>>
        handleUnauthorizedResourceAccess(
                UnauthorizedResourceAccessException exception) {

    Map<String, String> response =
            new HashMap<>();

    response.put(
            "error",
            "FORBIDDEN"
    );

    response.put(
            "message",
            exception.getMessage()
    );

    return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(response);
}

    // Your existing exception handlers go below this.
}