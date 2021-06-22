package app.dto.dialogs;

import app.dto.main.PaginationResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DialogResponse extends PaginationResponse {
    private List<DialogDto> data;

    @JsonProperty("users_ids")
    private List<Integer> userIds;

    public DialogResponse(List<Integer> userIds) {
        this.userIds = userIds;
    }

    public DialogResponse(int total, int perPage, List<DialogDto> data) {
        super(total, perPage);
        this.data = data;
    }

    public List<DialogDto> getData() {
        return data;
    }

    public List<Integer> getUserIds() {
        return userIds;
    }
}
