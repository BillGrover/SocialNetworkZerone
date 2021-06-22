package app.dto.likes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class LikeData implements Likes{

    @ApiModelProperty(value = "like", example = "1")
    private String like;
    private List<String> users;


    public LikeData(String like, List<String> users) {
        this.like = like;
        this.users = users;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "LikeData{" +
                "like='" + like + '\'' +
                ", users=" + users +
                '}';
    }
}
