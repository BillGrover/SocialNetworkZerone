package app.dto.tags;

import app.dto.main.PaginationResponse;

import java.util.List;

public class TagListResponse extends PaginationResponse {

    List<TagData> data;


    public TagListResponse(List<TagData> data) {
        this.data = data;
    }


    public List<TagData> getData() {
        return data;
    }

    public void setData(List<TagData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TagListResponse{" +
                "data=" + data +
                '}';
    }
}
