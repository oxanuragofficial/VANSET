package vanset_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CollectionNotFoundException extends RuntimeException {

    public CollectionNotFoundException(Long id) {
        super("Collection not found with id: " + id);
    }
}