package app.dto.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageRequest {
    @JsonProperty("message_text")
    private String messageText;

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
