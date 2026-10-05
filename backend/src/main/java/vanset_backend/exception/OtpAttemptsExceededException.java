package vanset_backend.exception;

public class OtpAttemptsExceededException
        extends RuntimeException {

    public OtpAttemptsExceededException(String message) {
        super(message);
    }
}