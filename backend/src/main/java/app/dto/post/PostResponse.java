package app.dto.post;

import app.dto.main.AppResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"error", "timestamp", "data"})
@ApiModel
public class PostResponse extends AppResponse {

    private PostResponseData data;


    public PostResponse(PostResponseData data) {
        super();
        this.data = data;
    }


    public PostResponseData getData() {
        return data;
    }

    public void setData(PostResponseData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PostResponse{" +
                "data=" + data +
                '}';
    }
}
