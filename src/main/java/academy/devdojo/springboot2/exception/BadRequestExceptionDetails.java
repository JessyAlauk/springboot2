package academy.devdojo.springboot2.exception;

import java.time.LocalDateTime;

public record BadRequestExceptionDetails (String title,
                                         int status,
                                         String details,
                                         String developerMessage,
                                         LocalDateTime timeStamp) {
}
