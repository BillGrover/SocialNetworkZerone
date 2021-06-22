package app.dto.post;

import app.config.AppConstant;
import app.dto.comment.CommentData;
import app.dto.profile.PersonDto;
import app.model.Person;
import app.model.Post;
import app.model.enums.BlockStatus;
import app.model.enums.PostStatus;
import com.fasterxml.jackson.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "time", "author", "title", "text", "block", "likes", "comments"})
@ApiModel
public class PostResponseData {

    @ApiModelProperty(value = "id", example = "1")
    private long id;

    @ApiModelProperty(value = "time", example = "1559751301818")
    private long time;

    private PersonDto author;

    @ApiModelProperty(value = "title", example = "string")
    private String title;

    @ApiModelProperty(value = "post_text", example = "string")
    @JsonProperty("post_text")
    private String text;

    @ApiModelProperty(value = "isBlocked", example = "false")
    @JsonProperty("is_blocked")
    private boolean block;

    @ApiModelProperty(value = "likes", example = "23")
    private int likes;

    private List<CommentData> comments;

    private List<String> tags;

    @JsonProperty("my_like")
    private boolean myLike;

    @JsonProperty("type")
    private PostStatus status;


    public PostResponseData(Post post, Person authUser)  {
        id = post.getId();
        time = post.getTime().getTime();
        author = new PersonDto(post.getAuthor());
        if (post.getAuthor().getBlock().equals(BlockStatus.TOTAL_BLOCKED)) {
            this.title = AppConstant.CONTENT_BLOCKED;
            this.text = AppConstant.CONTENT_BLOCKED;
            comments = new ArrayList<>();
            status = (post.getTime().before(new Date()))
                    ? PostStatus.POSTED
                    : PostStatus.QUEUED;
        } else {
            title = post.getTitle();
            text = post.getText();
            block = post.getBlock() == BlockStatus.BLOCKED;
            likes = post.getLikes().size();

            tags = new ArrayList<>();
            post.getPostToTags().forEach(post2Tag -> tags.add(post2Tag.getTag().getName()));

            this.myLike = false;
            post.getLikes().forEach(postLike -> {
                if (postLike.getPerson() == authUser) {
                    this.myLike = true;
                }
            });
            comments = new ArrayList<>();
            post.getComments().forEach(com -> comments.add(new CommentData(com, authUser)));
            status = (post.getTime().before(new Date()))
                    ? PostStatus.POSTED
                    : PostStatus.QUEUED;
        }

    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public PersonDto getAuthor() {
        return author;
    }

    public void setAuthor(PersonDto author) {
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

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public List<CommentData> getComments() {
        return comments;
    }

    public void setComments(List<CommentData> comments) {
        this.comments = comments;
    }

    public boolean isMyLike() {
        return myLike;
    }

    public void setMyLike(boolean myLike) {
        this.myLike = myLike;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public PostStatus getStatus() {
        return status;
    }

    public void setStatus(PostStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PostResponseData{" +
                "id=" + id +
                ", time=" + time +
                ", author=" + author +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", block=" + block +
                ", likes=" + likes +
                ", comments=" + comments +
                ", tags=" + tags +
                ", myLike=" + myLike +
                ", status=" + status +
                '}';
    }
}
