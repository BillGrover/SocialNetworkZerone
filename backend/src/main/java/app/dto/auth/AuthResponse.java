package app.dto.auth;

import app.dto.main.AppResponse;
import app.dto.profile.PersonDto;
import io.swagger.annotations.ApiModel;

@ApiModel
public class AuthResponse extends AppResponse {

    private PersonDto data;


    public PersonDto getData() {
        return data;
    }

    public void setData(PersonDto data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "data: = " + data +
                '}';
    }
}