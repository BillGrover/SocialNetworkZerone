package app.dto.likes;

import app.dto.main.AppResponse;

public class LikeResponse extends AppResponse {
    private Likes data;


    public LikeResponse(Likes data) {
        this.data = data;
    }


    public Likes getData() {
        return data;
    }

    public void setData(Likes data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LikeResponse{" +
                "data=" + data +
                '}';
    }
}
