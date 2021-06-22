package app.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class AddPostRequest {

    @ApiModelProperty(value = "title", example = "string")
    private String title;

    @ApiModelProperty(value = "post_text", example = "string")
    @JsonProperty("post_text")
    private String text;

    private List<String> tags;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "AddPostRequest{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", tags=" + tags +
                '}';
    }
}
