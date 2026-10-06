package vanset_backend.exception;

public class UnauthorizedResourceAccessException
        extends RuntimeException {

    public UnauthorizedResourceAccessException(
            String message) {

        super(message);
    }
}