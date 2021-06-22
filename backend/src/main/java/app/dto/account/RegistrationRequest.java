package app.dto.account;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class RegistrationRequest {

    @ApiModelProperty(value = "email", example = "arkady@example.com")
    private String email;

    @JsonProperty("passwd1")
    @ApiModelProperty(value = "passwd1", example = "123456")
    private String password;

    @JsonProperty("passwd2")
    @ApiModelProperty(value = "passwd2", example = "123456")
    private String passwdConfirm;

    @ApiModelProperty(value = "firstName", example = "Аркадий")
    private String firstName;

    @ApiModelProperty(value = "lastName", example = "Паровозов")
    private String lastName;

    @ApiModelProperty(value = "code", example = "3675")
    private Object data;


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswdConfirm() {
        return passwdConfirm;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return "RegistrationRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", passwdConfirm='" + passwdConfirm + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", data=" + data +
                '}';
    }
}