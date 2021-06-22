package app.dto.post;

import app.config.AppConstant;
import app.dto.comment.CommentData;
import app.dto.profile.PersonDto;
import app.model.Person;
import app.model.Post;
import app.model.enums.BlockStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

@ApiModel
public class PostDto {

    @ApiModelProperty(value = "id", example = "1")
    private long id;

    @ApiModelProperty(value = "time", example = "1559751301818")
    private long time;

    @ApiModelProperty(value = "author_id", example = "1")
    private PersonDto author;

    @ApiModelProperty(value = "title", example = "string")
    private String title;

    @JsonProperty("post_text")
    @ApiModelProperty(value = "post_text", example = "string")
    private String postText;

    @JsonProperty("is_blocked")
    @ApiModelProperty(value = "is_blocked", example = "true")
    private boolean block;

    @ApiModelProperty(value = "likes", example = "20")
    private int likes;

    private List<CommentData> comments;

    private List<String> tags;

    @JsonProperty("my_like")
    private boolean myLike;


    public PostDto(Post post, Person authUser)  {
        this.id = post.getId();
        this.time = post.getTime().getTime();
        this.author = new PersonDto(post.getAuthor());
        if (post.getAuthor().getBlock().equals(BlockStatus.TOTAL_BLOCKED)) {
            this.title = AppConstant.CONTENT_BLOCKED;
            this.postText = AppConstant.CONTENT_BLOCKED;
            comments = new ArrayList<>();
        } else {
            this.title = post.getTitle();
            this.postText = post.getText();
            this.block = post.getBlock() == BlockStatus.BLOCKED;
            this.likes = post.getLikes().size();
            this.myLike = false;
            post.getLikes().forEach(postLike -> {
                if (postLike.getPerson() == authUser) {
                    this.myLike = true;
                }
            });

            tags = new ArrayList<>();
            post.getPostToTags().forEach(post2Tag -> tags.add(post2Tag.getTag().getName()));
            comments = new ArrayList<>();
            post.getComments().forEach(com -> comments.add(new CommentData(com, authUser)));
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

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public boolean isMyLike() {
        return myLike;
    }

    public void setMyLike(boolean myLike) {
        this.myLike = myLike;
    }

    @Override
    public String toString() {
        return "PostDto{" +
                "id=" + id +
                ", time=" + time +
                ", author=" + author +
                ", title='" + title + '\'' +
                ", postText='" + postText + '\'' +
                ", block=" + block +
                ", likes=" + likes +
                ", comments=" + comments +
                ", tags=" + tags +
                ", myLike=" + myLike +
                '}';
    }


}