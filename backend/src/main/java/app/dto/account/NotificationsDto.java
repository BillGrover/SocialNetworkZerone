package app.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class NotificationsDto {

    @JsonProperty("enable")
    @ApiModelProperty(value = "enable", example = "true")
    private boolean enable;

    @JsonProperty("type")
    @ApiModelProperty(value = "type", example = "POST")
    private String type;


    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "NotificationsDto{" +
                "enable=" + enable +
                ", type='" + type + '\'' +
                '}';
    }
}

