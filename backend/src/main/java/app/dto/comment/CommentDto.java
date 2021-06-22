package app.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class CommentDto {
    @JsonProperty("parent_id")
    @ApiModelProperty(value = "parent_id", example = "1")
    private Long parentId;

    @JsonProperty("comment_text")
    @ApiModelProperty(value = "comment_text", example = "String")
    private String commentText;


    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    @Override
    public String toString() {
        return "CommentDto{" +
                "parentId=" + parentId +
                ", commentText='" + commentText + '\'' +
                '}';
    }
}
