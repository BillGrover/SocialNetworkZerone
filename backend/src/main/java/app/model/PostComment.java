package app.model;

import app.model.enums.BlockStatus;
import app.model.enums.DeleteStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "post_comments")
public class PostComment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private Date time;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private PostComment parentId;    //родительский комментарий (если ответ на комментарий к посту)

    @JsonIgnore
    @NotNull
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Person author;

    @NotNull
    private String text;

    @Column(name = "delete_date")
    private Date deleteDate;

    @JsonIgnore
    @OneToMany(mappedBy = "parentId")
    private List<PostComment> postCommentList;

    @JsonIgnore
    @OneToMany(targetEntity = PostLike.class, mappedBy = "postCommentId")
    private List<PostLike> postLikeList;

    @Enumerated(EnumType.STRING)
    @Column(name = "delete_status",
            nullable = false,
            columnDefinition = "enum('DELETED','UNDELETED') default 'UNDELETED'")
    private DeleteStatus deleteStatus;

    /**
     * Признак блокировки комментария модератором/администратором
     */
    @Enumerated(EnumType.STRING)
    private BlockStatus block;

    @OneToOne(mappedBy = "comment")
    private BlockHistory blockHistory;


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

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public PostComment getParentId() {
        return parentId;
    }

    public void setParentId(PostComment parentId) {
        this.parentId = parentId;
    }

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<PostComment> getPostCommentList() {
        return postCommentList;
    }

    public void setPostCommentList(List<PostComment> postCommentList) {
        this.postCommentList = postCommentList;
    }

    public List<PostLike> getPostLikeList() {
        return postLikeList;
    }

    public void setPostLikeList(List<PostLike> postLikeList) {
        this.postLikeList = postLikeList;
    }

    public DeleteStatus getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(DeleteStatus deleteStatus) {
        this.deleteStatus = deleteStatus;
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

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }
}
