package academy.devdojo.springboot2.domain;

import jakarta.persistence.*;

@Entity
public class Anime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String nameCharacter;

    public Anime(Long id, String nameCharacter) {
        this.nameCharacter = nameCharacter;
        this.id = id;
    }

    public Anime() {
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Anime{");
        sb.append("id=").append(id);
        sb.append(", nameCharacter='").append(nameCharacter).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getNameCharacter() {
        return nameCharacter;
    }

    public Long getId() {
        return id;
    }
}
