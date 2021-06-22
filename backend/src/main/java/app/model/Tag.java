package app.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;

    @OneToMany(mappedBy = "tag")
    private Set<PostToTag> postToTags;

    public Tag() {
    }

    public Tag(@NotNull String name) {
        this.name = name;
    }


    // GETTERS & SETTERS

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<PostToTag> getPost2tags() {
        return postToTags;
    }

    public void setPost2tags(Set<PostToTag> postToTags) {
        this.postToTags = postToTags;
    }
}
