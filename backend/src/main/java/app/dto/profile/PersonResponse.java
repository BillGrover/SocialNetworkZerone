package app.dto.profile;

import app.dto.main.AppResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

@ApiModel
public class PersonResponse extends AppResponse {

    @JsonProperty("data")
    private PersonDto personDto;


    public PersonDto getData() {
        return personDto;
    }

    public void setData(PersonDto data) {
        this.personDto = data;
    }

    @Override
    public String toString() {
        return "PersonResponse{" +
                "personDto=" + personDto +
                '}';
    }
}
