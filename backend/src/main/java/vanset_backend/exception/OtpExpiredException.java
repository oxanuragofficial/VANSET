package vanset_backend.exception;

public class OtpExpiredException
        extends RuntimeException {

    public OtpExpiredException(String message) {
        super(message);
    }
}