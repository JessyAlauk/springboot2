package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;
import academy.devdojo.springboot2.service.AnimeService;
import academy.devdojo.springboot2.util.AnimeCreator;
import academy.devdojo.springboot2.util.AnimePostRequestBodyCreator;
import academy.devdojo.springboot2.util.AnimePutRequestBodyCreator;
import academy.devdojo.springboot2.util.DateUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        DateUtil.class,
        AnimeController.class
})
class AnimeControllerTest {

    private static final PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));

    private static final List<Anime> animeList = List.of(AnimeCreator.createValidAnime(),
            AnimeCreator.createValidAnime(),
            AnimeCreator.createValidAnime());

    private static final List<Anime> anAnimeList = List.of(AnimeCreator.createValidAnime());

    private static final Anime anAnime = AnimeCreator.createValidAnime();

    private static final Anime newAnime = AnimeCreator.createAnimeToBeSaved();

    private static final Anime updatedAnime = AnimeCreator.createUpdatedAnime();

    @MockitoBean
    private AnimeService animeServiceMock;

    @Autowired
    private AnimeController animeController;


    @BeforeEach
    void setUp(){
        when(animeServiceMock.listAll(any())).thenReturn(animePage);
        when(animeServiceMock.listAllNonPageable()).thenReturn(animeList);
        when(animeServiceMock.findById(anyLong())).thenReturn(anAnime);
        when(animeServiceMock.findByName(any())).thenReturn(anAnimeList);
        when(animeServiceMock.save(any(AnimePostRequestBody.class))).thenReturn(newAnime);
        when(animeServiceMock.replace(any(AnimePutRequestBody.class))).thenReturn(updatedAnime);
        doNothing().when(animeServiceMock).delete(anyLong());
    }

    @Test
    @DisplayName("List returns list of anime inside page object when successful")
    void list_ReturnsListOfAnimesInsidePageObject_WhenSuccessful(){
        var newAnimePage = animeController.list(null).getContent();

        assertNotNull(newAnimePage);
        Assertions.assertThat(newAnimePage).isNotEmpty().hasSize(1);
        assertEquals(newAnimePage.getFirst().getName(), animePage.get().toList().getFirst().getName());

    }

    @Test
    @DisplayName("ListAll returns list of anime when successful")
    void listAll_ReturnsListOfAnimes_WhenSuccessful(){
        var animes = animeController.listAll().getBody();

        Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(3);
        assertEquals(animeList, animes);
    }

    @Test
    @DisplayName("FindById return anime when successful")
    void findById_ReturnAnime_WhenSuccessful(){
        var anime = animeController.findById(1L).getBody();

        assertNotNull(anime);
        assertEquals(anAnime.getId(), anime.getId());

    }

    @Test
    @DisplayName("FindByname return anime when successful")
    void findByName_ReturnAnime_WhenSuccessful(){
        var anime = animeController.findByName("anime").getBody();

        assertNotNull(anime);
        Assertions.assertThat(anime).isNotEmpty().hasSize(1);
        assertEquals(anime.getFirst().getName(), anAnimeList.getFirst().getName());

    }

    @Test
    @DisplayName("FindByname return an empty list of anime is not found")
    void findByName_ReturnEmptyListOfAnime_WhenAnimeIsNotFound(){
        when(animeServiceMock.findByName(any())).thenReturn(Collections.emptyList());
        var anime = animeController.findByName("anime").getBody();

        assertNotNull(anime);
        Assertions.assertThat(anime).isEmpty();

    }

    @Test
    @DisplayName("Save return anime when successful")
    void save_ReturnAnime_WhenSuccessful(){
        var anime = animeController.save(AnimePostRequestBodyCreator
                .createAnimePostRequestBody(newAnime.getName())).getBody();

        assertNotNull(anime);
        assertEquals(newAnime, anime);

    }

    @Test
    @DisplayName("Replace update anime when successful")
    void replace_UpdateAnime_WhenSuccessful(){
        var anime = animeController.replace(AnimePutRequestBodyCreator
                .createAnimePutRequestBody(updatedAnime.getId(), updatedAnime.getName())).getBody();

        assertNotNull(anime);
        assertEquals(updatedAnime, anime);

    }

    @Test
    @DisplayName("Delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccessful(){
        Assertions.assertThatCode(() -> animeController.delete(1)).doesNotThrowAnyException();
        var entity = animeController.delete(1);

        assertNotNull(entity);

        assertEquals(HttpStatus.NO_CONTENT, entity.getStatusCode());

    }
}