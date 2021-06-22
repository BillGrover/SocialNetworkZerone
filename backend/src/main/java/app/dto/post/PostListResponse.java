package app.dto.post;

import app.dto.main.PaginationResponse;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

public class PostListResponse extends PaginationResponse {

    @JsonInclude
    private List<PostResponseData> data;


    public PostListResponse() {
        data = new ArrayList<>();
    }


    public List<PostResponseData> getData() {
        return data;
    }

    public void setData(List<PostResponseData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PostListResponse{" +
                "data=" + data +
                '}';
    }
}
