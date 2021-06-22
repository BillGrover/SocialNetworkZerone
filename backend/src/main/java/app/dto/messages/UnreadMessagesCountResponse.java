package app.dto.messages;

import app.dto.main.AppResponse;

public class UnreadMessagesCountResponse extends AppResponse {
    private UnreadMessageCountDto data;

    public UnreadMessagesCountResponse(UnreadMessageCountDto data) {
        this.data = data;
    }

    public UnreadMessageCountDto getData() {
        return data;
    }
}
