package app.dto.platform;

import app.dto.location.CityDto;
import app.dto.main.PaginationResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

import java.util.List;

@ApiModel
public class CitiesResponse extends PaginationResponse {

    @JsonProperty("data")
    private List<CityDto> cities;


    public List<CityDto> getCities() {
        return cities;
    }

    public void setCities(List<CityDto> cities) {
        this.cities = cities;
    }

    @Override
    public String toString() {
        return "CitiesResponse{" +
                "cities=" + cities +
                '}';
    }
}
