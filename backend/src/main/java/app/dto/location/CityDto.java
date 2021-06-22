package app.dto.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class CityDto {

    @ApiModelProperty(value = "id", example = "1")
    private long id;

    @ApiModelProperty(value = "title", example = "Москва")
    @JsonProperty("title")
    private String name;


    public CityDto() {
    }

    public CityDto(long id, String name) {
        this.id = id;
        this.name = name;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CityDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
