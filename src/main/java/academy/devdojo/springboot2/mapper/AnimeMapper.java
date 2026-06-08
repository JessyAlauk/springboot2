package academy.devdojo.springboot2.mapper;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;

public final class AnimeMapper {

    private AnimeMapper() {
    }

    public static Anime toSave(AnimePostRequestBody body) {
        return new Anime(null, body.name());
    }

    public static Anime toAnime(AnimePutRequestBody body) {
        return new Anime(
                body.id(),
                body.name()
        );
    }
}
