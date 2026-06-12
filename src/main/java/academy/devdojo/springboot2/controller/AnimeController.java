package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;
import academy.devdojo.springboot2.service.AnimeService;
import academy.devdojo.springboot2.util.DateUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("animes")
public class AnimeController {
    private static final Logger LOG = LoggerFactory.getLogger(AnimeController.class);

    private final DateUtil dateUtil;
    private final AnimeService animeService;

    public AnimeController(DateUtil dateUtil, AnimeService animeService) {
        this.dateUtil = dateUtil;
        this.animeService = animeService;
    }

    @GetMapping
    public Page<Anime> list(Pageable pageable){
        LOG.info(dateUtil.formatLocalDateTimeTodatabaseStyle(LocalDateTime.now()));
        return animeService.listAll(pageable);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<Anime>> listAll(){
        LOG.info(dateUtil.formatLocalDateTimeTodatabaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok(animeService.listAllNonPageable());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> findById(@PathVariable long id){
        LOG.info(dateUtil.formatLocalDateTimeTodatabaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok().body(animeService.findById(id));
    }

    @GetMapping(path = "by-id/{id}")
    public ResponseEntity<Anime> findByIdAuthenticationPrincipal(@PathVariable long id,
                                                                 @AuthenticationPrincipal UserDetails userDetails){
        LOG.info(dateUtil.formatLocalDateTimeTodatabaseStyle(LocalDateTime.now()));
        LOG.info(userDetails.toString());
        return ResponseEntity.ok().body(animeService.findById(id));
    }

    @GetMapping(path = "/find")
    public ResponseEntity<List<Anime>> findByName(@RequestParam String name){
        LOG.info(dateUtil.formatLocalDateTimeTodatabaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok().body(animeService.findByName(name));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Anime> save(@RequestBody @Valid AnimePostRequestBody anime){
        LOG.info(dateUtil.formatLocalDateTimeTodatabaseStyle(LocalDateTime.now()));
        var response = animeService.save(anime);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Valid long id){
        LOG.info(dateUtil.formatLocalDateTimeTodatabaseStyle(LocalDateTime.now()));
        animeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Anime> replace(@RequestBody AnimePutRequestBody anime){
        LOG.info(dateUtil.formatLocalDateTimeTodatabaseStyle(LocalDateTime.now()));
        var response = animeService.replace(anime);
        return ResponseEntity.ok().body(response);
    }
}
