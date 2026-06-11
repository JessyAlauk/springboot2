package academy.devdojo.springboot2.repository;


import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.util.AnimeCreator;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@DisplayName("Tests for Anime Repository")
class AnimeRepositoryTest {

    private static final Logger LOG = LoggerFactory.getLogger(AnimeRepositoryTest.class);

    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("Save persists anime when Sucessful")
    void save_PersistAnime_WhenSuccessful(){
        var anime = AnimeCreator.createAnimeToBeSaved();
        var savedAnime = this.animeRepository.save(anime);

        assertNotNull(savedAnime);
        assertNotNull(savedAnime.getId());
        assertEquals(savedAnime.getName(), anime.getName());

//        Assertions.assertThat(savedAnime).isNotNull();
//        Assertions.assertThat(savedAnime.getId()).isNotNull();
//        Assertions.assertThat(savedAnime.getName()).isEqualTo(anime.getName());
    }

    @Test
    @DisplayName("Save updates anime when Sucessful")
    void save_UpdatesAnime_WhenSuccessful(){
        var oldAnime = AnimeCreator.createAnimeToBeSaved();
        var savedAnime = this.animeRepository.save(oldAnime);

        var newAnimeName = AnimeCreator.createValidAnime(savedAnime.getId(), "Attack on Titan");
        var updatedAnime = this.animeRepository.save(newAnimeName);

        Assertions.assertThat(updatedAnime).isNotNull();
        Assertions.assertThat(updatedAnime.getId()).isNotNull();
        Assertions.assertThat(updatedAnime.getName()).isEqualTo(newAnimeName.getName());

    }

    @Test
    @DisplayName("Delete removes anime when Sucessful")
    void delete_RemovesAnime_WhenSuccessful(){
        var anime = AnimeCreator.createAnimeToBeSaved();
        var savedAnime = this.animeRepository.save(anime);

        this.animeRepository.delete(savedAnime);

        var deletedAnime = this.animeRepository.findById(savedAnime.getId());

        Assertions.assertThat(deletedAnime.isEmpty()).isTrue();

        LOG.info(deletedAnime.toString());

    }

    @Test
    @DisplayName("Find By Name returns list of anime when Sucessful")
    void findByName_ReturnsListOfAnime_WhenSuccessful(){
        var anime = AnimeCreator.createAnimeToBeSaved();
        var savedAnime = this.animeRepository.save(anime);

        var name = savedAnime.getName();

        var animes = this.animeRepository.findByName(name);

        Assertions.assertThat(animes).isNotEmpty().contains(savedAnime);
    }

    @Test
    @DisplayName("Find By Name returns empty list when no anime is found")
    void findByName_ReturnsEmptyList_WhenAnimeIsNotFound(){
        var anime = AnimeCreator.createAnimeToBeSaved();
        this.animeRepository.save(anime);

        var animes = this.animeRepository.findByName("Fullmetal Alchemist");

        Assertions.assertThat(animes).isEmpty();
    }

    @Test
    @DisplayName("Save throw ConstraintValidationException when name is empty")
    void save_ThrowConstraintValidationException_WhenNameIsEmpty(){
        var anime = new Anime();

        assertThrows(ConstraintViolationException.class, () -> this.animeRepository.save(anime));
//        Assertions.assertThatThrownBy(() -> this.animeRepository.save(anime))
//                .isInstanceOf(ConstraintViolationException.class);

//        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
//                .isThrownBy(() -> this.animeRepository.save(anime))
//                .withMessage("The anime name cannot be empty");
    }

}