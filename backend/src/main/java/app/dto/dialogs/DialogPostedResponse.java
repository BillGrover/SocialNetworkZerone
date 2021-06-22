package app.dto.dialogs;

import app.dto.main.PaginationResponse;

public class DialogPostedResponse extends PaginationResponse {

    private DialogIdDto data;

    public DialogPostedResponse(int id) {
        this.data = new DialogIdDto(id);
    }

    public DialogIdDto getData() {
        return data;
    }

    public void setData(DialogIdDto data) {
        this.data = data;
    }
}
