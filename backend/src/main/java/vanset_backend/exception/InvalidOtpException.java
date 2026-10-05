package vanset_backend.exception;

public class InvalidOtpException
        extends RuntimeException {

    public InvalidOtpException(String message) {
        super(message);
    }
}