package app.dto.account;

import app.dto.main.AppResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class NotificationListResponse extends AppResponse {

    @JsonProperty("data")
    private List<NotificationsDto> notifications = new ArrayList<>();


    public List<NotificationsDto> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationsDto> notifications) {
        this.notifications = notifications;
    }

    @Override
    public String toString() {
        return "NotificationListResponse{" +
                "notifications=" + notifications +
                '}';
    }
}
