package academy.devdojo.springboot2.util;

import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import net.datafaker.Faker;

import java.util.Locale;

public class AnimePostRequestBodyCreator {

    private static final Faker FAKER = new Faker(Locale.of("pt-BR"));

    public static AnimePostRequestBody createAnimePostRequestBody(String name){
        return new AnimePostRequestBody(name);
    }
}
