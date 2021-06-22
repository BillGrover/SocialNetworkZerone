package app.dto.comment;

import app.dto.main.AppResponse;

public class CommentResponse extends AppResponse {

    CommentData data;


    public CommentResponse(CommentData data) {
        this.data = data;
    }


    public CommentData getData() {
        return data;
    }

    public void setData(CommentData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CommentResponse{" +
                "data=" + data +
                '}';
    }
}
