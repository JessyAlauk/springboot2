package academy.devdojo.springboot2.handler;

import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.exception.BadRequestExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestExceptionHandler {

    private static final String TITLE = "Bad Request Exception. Check the documentation.";

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestExceptionDetails> handlerBadRequestException(BadRequestException badRequestException) {
        return new ResponseEntity<>(
                new BadRequestExceptionDetails(
                        TITLE,
                        HttpStatus.BAD_REQUEST.value(),
                        badRequestException.getMessage(),
                        badRequestException.getClass().getName(),
                        LocalDateTime.now()
                ),
                HttpStatus.BAD_REQUEST
        );
    }
}
