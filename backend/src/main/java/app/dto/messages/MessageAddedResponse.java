package app.dto.messages;

import app.dto.main.AppResponse;

public class MessageAddedResponse extends AppResponse {

    private MessageDto data;

    public MessageAddedResponse(MessageDto data) {
        this.data = data;
    }

    public MessageDto getData() {
        return data;
    }
}
