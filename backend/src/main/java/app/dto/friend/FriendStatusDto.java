package app.dto.friend;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class FriendStatusDto {

    @JsonProperty(value = "user_id")
    @ApiModelProperty(value = "user_id", example = "1")
    private  long userId;

    @ApiModelProperty(value = "status", example = "FRIEND")
    private String status;


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "FriendStatusDto{" +
                "userId=" + userId +
                ", status='" + status + '\'' +
                '}';
    }
}
