package app.dto.friend;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FriendRequest {

    @JsonProperty("user_ids")
    private List<Long> userIds;


    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    @Override
    public String toString() {
        return "FriendRequest{" +
                "userIds=" + userIds +
                '}';
    }
}
