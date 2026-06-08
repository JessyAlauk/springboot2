package academy.devdojo.springboot2.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ErrorResponse(String title,
                            int status,
                            String message,
                            List<FieldErrorDetails> fieldsError,
                            LocalDateTime timeStamp) {

    public record FieldErrorDetails(String field,
                                    String message) {

    }

}
