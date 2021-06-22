package app.model;

import app.model.enums.BanStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

//Баны и разбаны пёрсонов
@Entity
@Table(name = "block_history")
public class BlockHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private Date time;

    @OneToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

    @OneToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    @OneToOne
    @JoinColumn(name = "post_comment_id", referencedColumnName = "id")
    private PostComment comment;

    @Enumerated(EnumType.STRING)
    private BanStatus banStatus;


    // GETTERS & SETTERS

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public PostComment getComment() {
        return comment;
    }

    public void setComment(PostComment comment) {
        this.comment = comment;
    }

    public BanStatus getBanStatus() {
        return banStatus;
    }

    public void setBanStatus(BanStatus banStatus) {
        this.banStatus = banStatus;
    }
}
