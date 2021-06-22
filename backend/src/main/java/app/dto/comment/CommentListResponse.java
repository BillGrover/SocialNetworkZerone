package app.dto.comment;

import app.dto.main.PaginationResponse;
import io.swagger.annotations.ApiModel;

import java.util.List;

@ApiModel
public class CommentListResponse extends PaginationResponse {

    private List<CommentData> data;


    public CommentListResponse(List<CommentData> data) {
        this.data = data;
    }


    public List<CommentData> getData() {
        return data;
    }

    public void setData(List<CommentData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CommentListResponse{" +
                "data=" + data +
                '}';
    }
}
