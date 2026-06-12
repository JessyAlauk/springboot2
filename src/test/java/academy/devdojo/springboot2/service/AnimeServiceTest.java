package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.exception.AnimeNotFoundException;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.util.AnimeCreator;
import academy.devdojo.springboot2.util.AnimePostRequestBodyCreator;
import academy.devdojo.springboot2.util.AnimePutRequestBodyCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        AnimeService.class
})
class AnimeServiceTest {

    private static final PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));

    private static final List<Anime> animeList = List.of(AnimeCreator.createValidAnime(),
            AnimeCreator.createValidAnime(),
            AnimeCreator.createValidAnime());

    private static final List<Anime> anAnimeList = List.of(AnimeCreator.createValidAnime());

    private static final Anime anAnime = AnimeCreator.createValidAnime();

    private static final Anime newAnime = AnimeCreator.createAnimeToBeSaved();

    private static final Anime updatedAnime = AnimeCreator.createUpdatedAnime();

    @MockitoBean
    private AnimeRepository animeRepositoryMock;

    @Autowired
    private AnimeService animeService;


    @BeforeEach
    void setUp(){
        when(animeRepositoryMock.findAll(any(PageRequest.class))).thenReturn(animePage);
        when(animeRepositoryMock.findAll()).thenReturn(animeList);
        when(animeRepositoryMock.findById(anyLong())).thenReturn(Optional.of(anAnime));
        when(animeRepositoryMock.findByName(any())).thenReturn(anAnimeList);
        when(animeRepositoryMock.save(any(Anime.class))).thenReturn(newAnime);
        doNothing().when(animeRepositoryMock).delete(any(Anime.class));
    }

    @Test
    @DisplayName("ListAll returns list of anime inside page object when successful")
    void listAll_ReturnsListOfAnimesInsidePageObject_WhenSuccessful(){
        var newAnimePage = animeService.listAll(PageRequest.of(1,1));

        assertNotNull(newAnimePage);
        Assertions.assertThat(newAnimePage).isNotEmpty().hasSize(1);
        assertEquals(newAnimePage.get().toList().getFirst().getName(), animePage.stream().toList().getFirst().getName());

    }

    @Test
    @DisplayName("ListAllNonPageable returns list of anime when successful")
    void listAllNonPageable_ReturnsListOfAnimes_WhenSuccessful(){
        var animes = animeService.listAllNonPageable();

        Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(3);
        assertEquals(animeList, animes);
    }

    @Test
    @DisplayName("FindById return anime when successful")
    void findById_ReturnAnime_WhenSuccessful(){
        var anime = animeService.findById(anAnime.getId());

        assertNotNull(anime);
        assertEquals(anAnime.getId(), anime.getId());

    }

    @Test
    @DisplayName("FindById throws BadRequestException when anime is not found")
    void findById_ThrowsBadRequestException_WhenAnimeIsNotFound(){
        when(animeRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(AnimeNotFoundException.class, () -> animeService.findById(1));

    }

    @Test
    @DisplayName("FindByName return anime when successful")
    void findByName_ReturnAnime_WhenSuccessful(){
        var animePage = animeService.findByName("anime");

        assertNotNull(animePage);
        Assertions.assertThat(animePage).isNotEmpty().hasSize(1);
        assertEquals(animePage.getFirst().getName(), anAnimeList.getFirst().getName());

    }

    @Test
    @DisplayName("FindByname return an empty list of anime is not found")
    void findByName_ReturnEmptyListOfAnime_WhenAnimeIsNotFound(){
        when(animeRepositoryMock.findByName(any())).thenReturn(Collections.emptyList());
        var animePage = animeService.findByName("anime");

        assertNotNull(animePage);
        Assertions.assertThat(animePage).isEmpty();

    }

    @Test
    @DisplayName("Save return anime when successful")
    void save_ReturnAnime_WhenSuccessful(){
        var anime = animeService.save(AnimePostRequestBodyCreator
                .createAnimePostRequestBody(newAnime.getName()));

        assertNotNull(anime);
        assertEquals(newAnime, anime);

    }

    @Test
    @DisplayName("Replace updates anime when successful")
    void replace_UpdatesAnime_WhenSuccessful(){
        Assertions.assertThatCode(() -> animeService.replace(AnimePutRequestBodyCreator
                .createAnimePutRequestBody(updatedAnime.getId(), updatedAnime.getName()))).doesNotThrowAnyException();

    }

    @Test
    @DisplayName("Delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccessful(){
        Assertions.assertThatCode(() -> animeService.delete(1)).doesNotThrowAnyException();
    }
}