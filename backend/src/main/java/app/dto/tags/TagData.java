package app.dto.tags;

import app.model.Tag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class TagData {

    @ApiModelProperty(value = "id", example = "1")
    Long id;

    @ApiModelProperty(value = "tag", example = "String")
    String tag;


    public TagData(Tag tag) {
        this.id = tag.getId();
        this.tag = tag.getName();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "TagData{" +
                "id=" + id +
                ", tag='" + tag + '\'' +
                '}';
    }
}
