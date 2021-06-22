package app.dto;

import app.dto.main.PaginationResponse;
import app.dto.post.PostDto;
import io.swagger.annotations.ApiModel;

import java.util.List;

@ApiModel
public class FeedsResponseDto extends PaginationResponse {

    private List<PostDto> data;


    public List<PostDto> getData() {
        return data;
    }

    public void setData(List<PostDto> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "FeedsResponseDto{" +
                "data=" + data +
                '}';
    }
}
