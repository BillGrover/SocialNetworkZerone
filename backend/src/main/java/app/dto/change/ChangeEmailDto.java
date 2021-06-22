package app.dto.change;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ChangeEmailDto {

    @ApiModelProperty(value = "email", example = "arkady-ukupnik@thebest.com")
    private String email;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ChangeEmailDto{" +
                "email='" + email + '\'' +
                '}';
    }
}
