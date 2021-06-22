package app.dto.notification;

import app.dto.main.PaginationResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;

import java.util.List;

@ApiModel
@JsonInclude(JsonInclude.Include.NON_EMPTY)

public class NotificationListResponse extends PaginationResponse {

    private List<NotificationDto> data;


    public List<NotificationDto> getData() {
        return data;
    }

    public void setData(List<NotificationDto> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "NotificationListResponse{" +
                "data=" + data +
                '}';
    }
}
