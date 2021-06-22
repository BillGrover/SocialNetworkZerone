package app.dto.change;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ChangePasswordDto {

    @ApiModelProperty(value = "password", example = "123456")
    private String password;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "ChangePasswordDto{" +
                "password='" + password + '\'' +
                '}';
    }
}
