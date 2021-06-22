package app.dto.comment;

import app.config.AppConstant;
import app.dto.post.AuthorData;
import app.model.*;
import app.model.enums.BlockStatus;
import app.model.enums.DeleteStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"id", "parent_id", "comment_text", "post_id", "time", "author_id", "is_blocked"})
@ApiModel
public class CommentData {
    @ApiModelProperty(value = "id", example = "111")
    private long id;

    @ApiModelProperty(value = "time", example = "1559751301818")
    private Long time;

    @JsonProperty("post_id")
    @ApiModelProperty(value = "post_id", example = "string")
    private String postId;

    @JsonProperty("parent_id")
    @ApiModelProperty(value = "parent_id", example = "1")
    private Long parentId;

    @JsonProperty("author_id")
    @ApiModelProperty(value = "author_id", example = "1")
    private long authorId;

    @JsonProperty("author")
    @ApiModelProperty(value = "author_id", example = "1")
    private AuthorData author;

    @JsonProperty("comment_text")
    @ApiModelProperty(value = "comment_text", example = "string")
    private String text;

    @JsonProperty("is_blocked")
    @ApiModelProperty(value = "is_blocked", example = "true")
    private boolean isBlocked;

    @JsonProperty("is_deleted")
    @ApiModelProperty(value = "is_blocked", example = "true")
    private boolean isDeleted;

    @JsonProperty("sub_comments")
    private List<CommentData> comments;

    @JsonProperty("is_my_like")
    private boolean myLike;

    @JsonProperty("like_count")
    private Integer likes;


    public CommentData(PostComment postComment, Person authUser) {

        if (postComment.getAuthor().getBlock().equals(BlockStatus.TOTAL_BLOCKED)){
            text = AppConstant.CONTENT_BLOCKED;
            this.comments = new ArrayList<>();
            this.myLike = false;
            likes = null;
        }else {
            this.text = postComment.getText();
            this.comments = new ArrayList<>();
        fillingComments(comments, postComment, authUser);
            this.myLike = false;
            isAuthlike(postComment, authUser);
            likes = postComment.getPostLikeList() == null ? null : postComment.getPostLikeList().size();
        }
        this.id = postComment.getId();
        this.time = postComment.getTime().getTime();
        this.postId = postComment.getPost() == null ? String.valueOf(postComment.getParentId().getPost().getId()) : String.valueOf(postComment.getPost().getId());
        this.parentId = postComment.getParentId() == null ? null : postComment.getParentId().getId();
        this.authorId = postComment.getAuthor().getId();
        this.isBlocked = postComment.getBlock() == BlockStatus.BLOCKED;
        this.author = new AuthorData(postComment.getAuthor());
        isDeleted = postComment.getDeleteStatus() == DeleteStatus.DELETED;


    }


    public AuthorData getAuthor() {
        return author;
    }

    public void setAuthor(AuthorData author) {
        this.author = author;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    private void fillingComments(List<CommentData> commentDataList, PostComment postComment, Person authUser) {
        if (postComment.getPostCommentList() != null) {
            postComment.getPostCommentList().forEach(com -> {
                if (com.getDeleteStatus().equals(DeleteStatus.UNDELETED)) {
                    commentDataList.add(new CommentData(com, authUser));
                }
            });
        }
    }

    private void isAuthlike(PostComment postComment, Person authUser) {
        List<PostLike> postLikeList = postComment.getPostLikeList();
        if (postLikeList != null) {
            postLikeList.forEach(like -> {
                if (like.getPerson() == authUser) {
                    myLike = true;
                }
            });
        }
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

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public boolean isIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "CommentData{" +
                "id=" + id +
                ", time=" + time +
                ", postId='" + postId + '\'' +
                ", parentId=" + parentId +
                ", authorId=" + authorId +
                ", author=" + author +
                ", text='" + text + '\'' +
                ", isBlocked=" + isBlocked +
                ", isDeleted=" + isDeleted +
                ", comments=" + comments +
                ", myLike=" + myLike +
                ", likes=" + likes +
                '}';
    }
}
