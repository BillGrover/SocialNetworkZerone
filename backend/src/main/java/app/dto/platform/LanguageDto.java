package app.dto.platform;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class LanguageDto {

    @ApiModelProperty(value = "id", example = "1")
    private long id;

    @JsonProperty("title")
    @ApiModelProperty(value = "title", example = "Русский")
    private String language;


    public LanguageDto(long id, String language) {
        this.id = id;
        this.language = language;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "LanguageDto{" +
                "id=" + id +
                ", language='" + language + '\'' +
                '}';
    }
}
