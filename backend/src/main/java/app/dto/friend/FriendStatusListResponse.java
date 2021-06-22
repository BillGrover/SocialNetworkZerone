package app.dto.friend;

import app.dto.main.AppResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel
public class FriendStatusListResponse extends AppResponse {

    private List<FriendStatusDto> data;


    public List<FriendStatusDto> getData() {
        return data;
    }

    public void setData(List<FriendStatusDto> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "FriendStatusListResponse{" +
                "data=" + data +
                '}';
    }
}
