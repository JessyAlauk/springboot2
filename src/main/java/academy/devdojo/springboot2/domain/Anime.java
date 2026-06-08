package academy.devdojo.springboot2.domain;

import jakarta.persistence.*;

@Entity
public class Anime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    public Anime(Long id, String name) {
        this.name = name;
        this.id = id;
    }

    public Anime() {
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Anime{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}
