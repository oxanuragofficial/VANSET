package vanset_backend.exception;

public class OtpRateLimitException
        extends RuntimeException {

    public OtpRateLimitException(String message) {
        super(message);
    }
}