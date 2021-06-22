package app.model;

import app.model.enums.BlockStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "posts")
public class Post implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Дата и время публикаци
     */
    @NotNull
    private Date time;


    @ManyToOne
    @JoinColumn(name = "author_id")
    private Person author;

    /**
     * Заголовок поста
     */
    @NotNull
    private String title;

    /**
     * HTML-текст поста
     */
    @NotNull
    private String text;

    /**
     * Признак блокировки поста модератором/администратором
     */
    @Enumerated(EnumType.STRING)
    private BlockStatus block;


    @OneToOne(mappedBy = "post")
    private BlockHistory blockHistory;


    @OneToMany(mappedBy = "post")
    private List<PostToTag> postToTags;

    @OneToMany(mappedBy = "post")
    private List<PostLike> likes;


    @OneToMany(mappedBy = "post")
    private List<PostFile> files;


    @OneToMany(mappedBy = "post")
    private List<PostComment> comments;


    public Post() {
        this.block = BlockStatus.UNBLOCKED;
        this.postToTags = new ArrayList<>();
        this.likes = new ArrayList<>();
        this.files = new ArrayList<>();
        this.comments = new ArrayList<>();
    }


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

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public BlockStatus getBlock() {
        return block;
    }

    public void setBlock(BlockStatus block) {
        this.block = block;
    }

    public BlockHistory getBlockHistory() {
        return blockHistory;
    }

    public void setBlockHistory(BlockHistory blockHistory) {
        this.blockHistory = blockHistory;
    }

    public List<PostToTag> getPostToTags() {
        return postToTags;
    }

    public void setPostToTags(List<PostToTag> postToTags) {
        this.postToTags = postToTags;
    }

    public List<PostLike> getLikes() {
        return likes;
    }

    public void setLikes(List<PostLike> likes) {
        this.likes = likes;
    }

    public List<PostFile> getFiles() {
        return files;
    }

    public void setFiles(List<PostFile> files) {
        this.files = files;
    }

    public List<PostComment> getComments() {
        return comments;
    }

    public void setComments(List<PostComment> comments) {
        this.comments = comments;
    }
}
