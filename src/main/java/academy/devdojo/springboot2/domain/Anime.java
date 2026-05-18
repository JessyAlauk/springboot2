package academy.devdojo.springboot2.domain;

public class Anime {
    private Long id;
    private String name;

    public Anime(Long id, String name) {
        this.name = name;
        this.id = id;
    }

    public Anime() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
