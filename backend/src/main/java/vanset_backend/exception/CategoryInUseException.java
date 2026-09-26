package vanset_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CategoryInUseException extends RuntimeException {

    public CategoryInUseException(Long id) {
        super("Category cannot be deleted because products are using it: " + id);
    }
}