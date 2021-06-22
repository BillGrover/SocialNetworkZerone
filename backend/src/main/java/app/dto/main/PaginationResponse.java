package app.dto.main;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

public abstract class PaginationResponse extends AppResponse {

    @ApiModelProperty(value = "total", example = "0")
    private int total;

    @JsonProperty("offset")
    @ApiModelProperty(value = "page", example = "1")
    private int offset;

    @ApiModelProperty(value = "perPage", example = "20")
    private int perPage;

    protected PaginationResponse() {
        super();
        this.offset = 0;
    }

    protected PaginationResponse(int total, int perPage) {
        this();
        this.total = total;
        this.perPage = perPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    @Override
    public String toString() {
        return "PaginationResponse{" +
                "total=" + total +
                ", offset=" + offset +
                ", perPage=" + perPage +
                '}';
    }
}
