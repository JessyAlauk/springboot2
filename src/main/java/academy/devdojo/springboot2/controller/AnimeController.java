package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("anime")
public class AnimeController {
    private static final Logger LOG = LoggerFactory.getLogger(AnimeController.class);

    private final DateUtil dateUtil;

    public AnimeController(DateUtil dateUtil) {
        this.dateUtil = dateUtil;
    }

    @GetMapping(path = "list")
    public List<Anime> list(){
        LOG.info(dateUtil.formatLocalDateTimeTodatabaseStyle(LocalDateTime.now()));
        return List.of(new Anime("DBZ"), new Anime("Naruto"));
    }
}
