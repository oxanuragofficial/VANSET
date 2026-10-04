package vanset_backend.exception;

public class InvalidIdentifierException
        extends RuntimeException {

    public InvalidIdentifierException(String message) {
        super(message);
    }
}