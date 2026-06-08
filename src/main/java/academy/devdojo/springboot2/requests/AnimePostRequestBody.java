package academy.devdojo.springboot2.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AnimePostRequestBody(@NotNull(message = "The anime name cannot be null")
                                   @NotEmpty(message = "The anime name cannot be empty")
                                   String name) {
}
