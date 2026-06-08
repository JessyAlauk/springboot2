package academy.devdojo.springboot2.handler;

import academy.devdojo.springboot2.exception.AnimeNotFoundException;
import academy.devdojo.springboot2.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler {

    private static final String TITLE = "Anime not found. Check the documentation.";
    private static final String GENERIC_TITLE = "Bad Request. Check the documentation.";

    @ExceptionHandler(AnimeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerAnimeNotFoundException(AnimeNotFoundException exception) {
        var status = HttpStatus.NOT_FOUND;
        var response = mountErrorResponse(TITLE, status, exception);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handlerGenericException(Exception exception) {
        var status = HttpStatus.BAD_REQUEST;
        var response = mountErrorResponse(GENERIC_TITLE, status, exception);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

        var fieldErrors = exception.getBindingResult().getFieldErrors();
        var currentFields = new ArrayList<ErrorResponse.FieldErrorDetails>();
        fieldErrors.forEach(f -> currentFields.add(new ErrorResponse.FieldErrorDetails(f.getField(), f.getDefaultMessage())));
        var status = HttpStatus.BAD_REQUEST;
        var response = mountErrorResponse(GENERIC_TITLE, status, currentFields, exception);
        return ResponseEntity.status(status).body(response);
    }

    private ErrorResponse mountErrorResponse(String title,
                                             HttpStatus status,
                                             Exception ex) {
        return mountErrorResponse(
                title,
                status,
                List.of(),
                ex
        );
    }

    private ErrorResponse mountErrorResponse(String title,
                                             HttpStatus status,
                                             List<ErrorResponse.FieldErrorDetails> errorDetails,
                                             Exception ex) {
        return new ErrorResponse(
                title,
                status.value(),
                ex.getMessage(),
                errorDetails,
                LocalDateTime.now()
        );
    }


}
