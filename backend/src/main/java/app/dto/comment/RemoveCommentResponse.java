package app.dto.comment;

import app.dto.main.AppResponse;

public class RemoveCommentResponse extends AppResponse {

    private RemoveCommentData data;


    public RemoveCommentResponse(RemoveCommentData data) {
        this.data = data;
    }


    public RemoveCommentData getData() {
        return data;
    }

    public void setData(RemoveCommentData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RemoveCommentResponse{" +
                "data=" + data +
                '}';
    }
}
