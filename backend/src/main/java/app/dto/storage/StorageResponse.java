package app.dto.storage;

import app.dto.main.AppResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel
public class StorageResponse extends AppResponse {

    private StorageResponseData data;


    public StorageResponse(StorageResponseData data) {
        this.data = data;
    }


    public StorageResponseData getData() {
        return data;
    }

    public void setData(StorageResponseData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "StorageResponse{" +
                "data=" + data +
                '}';
    }
}
