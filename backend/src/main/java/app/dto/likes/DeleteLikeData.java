package app.dto.likes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class DeleteLikeData implements Likes {

    @ApiModelProperty(value = "like", example = "1")
    private String like;


    public DeleteLikeData(String like) {
        this.like = like;
    }


    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    @Override
    public String toString() {
        return "DeleteLikeData{" +
                "like='" + like + '\'' +
                '}';
    }
}
