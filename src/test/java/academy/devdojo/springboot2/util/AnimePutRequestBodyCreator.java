package academy.devdojo.springboot2.util;

import academy.devdojo.springboot2.requests.AnimePutRequestBody;
import net.datafaker.Faker;

import java.util.Locale;

public class AnimePutRequestBodyCreator {

    private static final Faker FAKER = new Faker(Locale.of("pt-BR"));

    public static AnimePutRequestBody createAnimePutRequestBody(Long id, String name){
        return new AnimePutRequestBody(id,name);
    }
}
