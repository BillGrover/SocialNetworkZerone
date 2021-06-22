package app.dto.tags;

import app.dto.main.AppResponse;

public class TagResponse extends AppResponse {

    TagData data;


    public TagResponse(TagData data) {
        this.data = data;
    }


    public TagResponse() {
    }

    public TagData getData() {
        return data;
    }

    public void setData(TagData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TagResponse{" +
                "data=" + data +
                '}';
    }
}
