package app.dto.likes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class IsLikeData implements Likes {

    @ApiModelProperty(value = "like", example = "false")
    private boolean like;


    public IsLikeData(boolean like) {
        this.like = like;
    }


    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    @Override
    public String toString() {
        return "IsLikeData{" +
                "like=" + like +
                '}';
    }
}
