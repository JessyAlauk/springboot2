package academy.devdojo.springboot2.requests;

import jakarta.validation.constraints.NotEmpty;

public record AnimePutRequestBody(Long id,
                                  @NotEmpty(message = "The anime name cannot be empty")
                                  String name) {
}
