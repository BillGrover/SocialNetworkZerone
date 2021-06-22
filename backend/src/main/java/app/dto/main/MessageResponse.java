package app.dto.main;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageResponse extends AppResponse {

    @JsonProperty("data")
    private Message message;


    public MessageResponse(String message) {
        this.message = new Message(message);
    }


    public static MessageResponse ok() {
        return new MessageResponse("ok");
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessageResponse{" +
                "message=" + message +
                '}';
    }
}
