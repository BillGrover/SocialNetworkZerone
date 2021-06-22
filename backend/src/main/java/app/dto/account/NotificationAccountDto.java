package app.dto.account;

import app.model.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class NotificationAccountDto {

    @JsonProperty("notification_type")
    @ApiModelProperty(value = "notification_type", example = "POST")
    private NotificationType type;
    @ApiModelProperty(value = "enable", example = "true")
    private boolean enable;

    public NotificationAccountDto(NotificationType type, boolean enable) {
        this.type = type;
        this.enable = enable;
    }


    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "NotificationAccountDto{" +
                "type=" + type +
                ", enable=" + enable +
                '}';
    }
}
