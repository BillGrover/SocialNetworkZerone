package app.dto.main;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ErrorResponse {
    @ApiModelProperty(value = "error", example = "invalid_request")
    private String error;

    @ApiModelProperty(value = "error_description", example = "string")
    @JsonProperty("statusText")
    private String description;


    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "error='" + error + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

