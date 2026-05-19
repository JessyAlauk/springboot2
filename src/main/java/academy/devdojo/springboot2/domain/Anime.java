package academy.devdojo.springboot2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Anime {
    private Long id;
    @JsonProperty("name")
    private String nameCaracter;

    public Anime(Long id, String nameCaracter) {
        this.nameCaracter = nameCaracter;
        this.id = id;
    }

    public Anime() {
    }

    public String getNameCaracter() {
        return nameCaracter;
    }

    public void setNameCaracter(String nameCaracter) {
        this.nameCaracter = nameCaracter;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
