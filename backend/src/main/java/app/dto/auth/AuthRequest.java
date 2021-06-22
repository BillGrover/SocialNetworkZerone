package app.dto.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Данные из базы Person для аунтификации
 *
 * @author Anmiha
 * @version 1.0
 */
@ApiModel
public class AuthRequest {

    @ApiModelProperty(value = "email", example = "9824104901@mail.ru")
    private String email;

    @ApiModelProperty(value = "password", example = "administrator")
    private String password;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AuthRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}


