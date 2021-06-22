package app.dto.platform;

import app.dto.location.CountryDto;
import app.dto.main.PaginationResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

import java.util.List;

@ApiModel
public class CountriesResponse extends PaginationResponse {

    @JsonProperty("data")
    private List<CountryDto> countries;


    public List<CountryDto> getCountries() {
        return countries;
    }

    public void setCountries(List<CountryDto> countries) {
        this.countries = countries;
    }

    @Override
    public String toString() {
        return "CountriesResponse{" +
                "countries=" + countries +
                '}';
    }
}
