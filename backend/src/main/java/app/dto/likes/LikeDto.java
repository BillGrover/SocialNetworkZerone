package app.dto.likes;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class LikeDto {

    @JsonProperty("item_id")
    @ApiModelProperty(value = "item_id", example = "1")
    private Long itemId;

    @ApiModelProperty(value = "type", example = "String")
    private String type;


    public LikeDto(Long itemId, String type) {
        this.itemId = itemId;
        this.type = type;
    }


    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "LikeDto{" +
                "itemId=" + itemId +
                ", type='" + type + '\'' +
                '}';
    }
}
