package vanset_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleProductNotFound(ProductNotFoundException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleCategoryNotFound(CategoryNotFoundException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(ProductVariantNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleProductVariantNotFound(
            ProductVariantNotFoundException exception) {

        return exception.getMessage();
    }
}