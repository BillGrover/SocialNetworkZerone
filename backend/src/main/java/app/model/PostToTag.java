package app.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "post2tag")
public class PostToTag implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public PostToTag() {
    }

    public PostToTag(@NotNull Post post, @NotNull Tag tag) {
        this.post = post;
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostToTag postToTag = (PostToTag) o;
        return post.equals(postToTag.post) && tag.equals(postToTag.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(post, tag);
    }

}
