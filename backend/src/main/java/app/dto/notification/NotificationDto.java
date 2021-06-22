package app.dto.notification;

import app.dto.profile.PersonDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class NotificationDto {

    @ApiModelProperty(value = "id", example = "1")
    private long id;

    @JsonProperty(value = "sent_time")
    @ApiModelProperty(value = "sent_time", example = "1559751301818")
    private long sentTime;

    @ApiModelProperty(value = "info", example = "String")
    private String info;

    @JsonProperty(value = "read_status")
    @ApiModelProperty(value = "read_status", example = "SENT")
    private String readStatus = "SENT";

    @JsonProperty(value = "event_type")
    @ApiModelProperty(value = "event_type", example = "POST")
    private String eventType;

    @JsonProperty(value = "entity_author")
    @ApiModelProperty(value = "entity_author", example = "1")
    private PersonDto entityAuthor;


    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public PersonDto getEntityAuthor() {
        return entityAuthor;
    }

    public void setEntityAuthor(PersonDto entityAuthor) {
        this.entityAuthor = entityAuthor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSentTime() {
        return sentTime;
    }

    public void setSentTime(long sentTime) {
        this.sentTime = sentTime;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "NotificationDto{" +
                "id=" + id +
                ", sentTime=" + sentTime +
                ", info='" + info + '\'' +
                ", readStatus='" + readStatus + '\'' +
                ", eventType='" + eventType + '\'' +
                ", entityAuthor=" + entityAuthor +
                '}';
    }
}
