package app.dto.dialogs;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DialogRequest {
    @JsonProperty("users_ids")
    private List<Integer> usersIds;

    public List<Integer> getUsersIds() {
        return usersIds;
    }
}
