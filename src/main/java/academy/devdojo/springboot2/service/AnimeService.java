package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AnimeService {

    private final AnimeRepository animeRepository;

    public AnimeService(AnimeRepository animeRepository) {
        this.animeRepository = animeRepository;
    }

    public List<Anime> listAll(){
        return animeRepository.findAll();
    }

    public Anime findById(long id){
        return animeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found"));
    }

    public Anime save(AnimePostRequestBody anime) {
        var newAnime = new Anime(null, anime.name());
        return animeRepository.save(newAnime);
    }

    public void delete(long id) {
        animeRepository.delete(findById(id));
    }

    public Anime replace(AnimePutRequestBody anime) {
        var savedAnime = findById(anime.id());
        var updatedAnime = new Anime(savedAnime.getId(), anime.name());
        return animeRepository.save(updatedAnime);
    }
}
