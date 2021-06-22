package app.dto.main;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class Message {
    @ApiModelProperty(value = "message", example = "ok")
    private String messageText;


    public Message(String message) {
        this.messageText = message;
    }


    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageText='" + messageText + '\'' +
                '}';
    }
}
