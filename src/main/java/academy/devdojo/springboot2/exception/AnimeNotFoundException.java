package academy.devdojo.springboot2.exception;

public class AnimeNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Anime not found by id: %d";

    public AnimeNotFoundException(Long id) {
        super(String.format(DEFAULT_MESSAGE, id));
    }
}
