package app.dto.main;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"errorReason", "timestamp", "data"})
@ApiModel
public abstract class AppResponse {

    @ApiModelProperty(value = "error", example = "string")
    private String error;

    @ApiModelProperty(value = "timestamp", example = "1559751301818")
    private long timestamp;


    protected AppResponse() {
        this.timestamp = System.currentTimeMillis();
    }


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "AppResponse{" +
                "error='" + error + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
