package academy.devdojo.springboot2.integration;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.util.AnimeCreator;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class AnimeControllerIT {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {

        RestAssured.port = port;

        RestAssured.basePath = "/animes";
    }

    @Test
    @DisplayName("List returns list of anime inside page object when successful")
    void list_ReturnsListOfAnimesInsidePageObject_WhenSuccessful(){
        createNewAnime();
        createNewAnime();

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("content", notNullValue())
                .extract()
                .response()
                .jsonPath()
                .prettyPrint();

    }

    @Test
    @DisplayName("ListAll returns list of anime  when successful")
    void listAll_ReturnsListOfAnimes_WhenSuccessful(){
        createNewAnime();
        createNewAnime();

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/all")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("", notNullValue())
                .extract()
                .response()
                .jsonPath()
                .prettyPrint();
    }

    @Test
    @DisplayName("FindById return anime when successful")
    void findById_ReturnAnime_WhenSuccessful(){
        var newAnime = createNewAnime();

        RestAssured.given()
                .contentType(ContentType.JSON)
                .pathParam("id", newAnime.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("name", equalTo(newAnime.getName()))
                .extract()
                .response()
                .jsonPath()
                .prettyPrint();
    }

    @Test
    @DisplayName("FindByname return anime when successful")
    void findByName_ReturnAnime_WhenSuccessful(){
        var newAnime = createNewAnime();

        var result = RestAssured.given()
                .contentType(ContentType.JSON)
                .param("name", newAnime.getName())
                .when()
                .get("find")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(new TypeRef<List<Anime>>() {});

        assertThat(result, hasItem(
                new Anime(newAnime.getId(), newAnime.getName())
        ));
    }

    @Test
    @DisplayName("FindByname return an empty list of anime is not found")
    void findByName_ReturnEmptyListOfAnime_WhenAnimeIsNotFound(){
        createNewAnime();
        var unknownName = "There is no";

        var result = RestAssured.given()
                .contentType(ContentType.JSON)
                .param("name", unknownName)
                .when()
                .get("find")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(new TypeRef<List<Anime>>() {});

        assertNotNull(result);
        assertThat(result, empty());
    }

    @Test
    @DisplayName("Save return anime when successful")
    void save_ReturnAnime_WhenSuccessful(){
        var anime = AnimeCreator.createAnimeToBeSaved();
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(anime)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("name", equalTo(anime.getName()))
                .extract()
                .response()
                .jsonPath()
                .prettyPrint();

    }

    @Test
    @DisplayName("Replace update anime when successful")
    void replace_UpdateAnime_WhenSuccessful(){
        var anime = createNewAnime();
        var updatedAnime = AnimeCreator.createUpdatedAnime(anime.getId());

        var result = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(updatedAnime)
                .when()
                .put()
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response()
                .as(Anime.class);

        assertNotNull(result);
        assertEquals(result.getName(), updatedAnime.getName());
        assertEquals(result.getId(), anime.getId());
    }

    @Test
    @DisplayName("Delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccessful(){
        var anime = createNewAnime();
        RestAssured.given()
                .contentType(ContentType.JSON)
                .pathParam("id", anime.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract()
                .response()
                .asPrettyString();

        var result = findAnimeById(anime.getId());
        assertNull(result.getId());
        assertNull(result.getName());
    }

    private Anime createNewAnime(){
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(AnimeCreator.createAnimeToBeSaved())
                .when()
                .post()
                .then()
                .extract()
                .body()
                .as(Anime.class);
    }

    private Anime findAnimeById(Long id){
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .when()
                .get("{id}")
                .then()
                .extract()
                .as(Anime.class);
    }

}
