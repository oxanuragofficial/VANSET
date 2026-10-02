package vanset_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AddressNotFoundException extends RuntimeException {

    public AddressNotFoundException(Long id) {
        super("Address not found with id: " + id);
    }
}