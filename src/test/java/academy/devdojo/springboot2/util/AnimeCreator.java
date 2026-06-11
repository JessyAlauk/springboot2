package academy.devdojo.springboot2.util;

import academy.devdojo.springboot2.domain.Anime;
import net.datafaker.Faker;

import java.util.Locale;

public class AnimeCreator {

    private static final Faker FAKER = new Faker(Locale.of("pt-BR"));

    public static Anime createAnimeToBeSaved(){
        return new Anime(null, fullMeatlAlchemist());
    }

    public static Anime createValidAnime(){
        return new Anime(1L, dragonBall());
    }

    public static Anime createValidAnime(Long id, String name){
        return new Anime(id, name);
    }

    public static Anime createUpdatedAnime(){
        return new Anime(1L, naruto());
    }

    private static String naruto(){
        return FAKER.naruto().character();
    }

    private static String dragonBall(){
        return FAKER.dragonBall().character();
    }

    private static String fullMeatlAlchemist(){
        return FAKER.fullMetalAlchemist().character();
    }
}
