package app.dto.tags;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class TagDto {

    @ApiModelProperty(value = "tag", example = "String")
    String tag;


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "TagDto{" +
                "tag='" + tag + '\'' +
                '}';
    }
}
