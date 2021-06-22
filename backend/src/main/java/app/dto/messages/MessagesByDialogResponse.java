package app.dto.messages;

import app.dto.main.PaginationResponse;

import java.util.List;

public class MessagesByDialogResponse extends PaginationResponse {
    private List<MessageDto> data;

    public MessagesByDialogResponse(int total, int perPage, List<MessageDto> data) {
        super(total, perPage);
        this.data = data;
    }

    public List<MessageDto> getData() {
        return data;
    }
}
