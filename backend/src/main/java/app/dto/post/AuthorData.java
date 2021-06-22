package app.dto.post;

import app.model.Person;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class AuthorData {
    @JsonProperty("first_name")
    @ApiModelProperty(value = "first_name", example = "Петр")
    private String firstName;

    @JsonProperty("last_name")
    @ApiModelProperty(value = "last_name", example = "Петрович")
    private String lastName;

    @JsonProperty("id")
    @ApiModelProperty(value = "id", example = "1")
    private long id;

    private String photo;


    public AuthorData(Person person) {
        id = person.getId();
        firstName = person.getFirstName();
        lastName = person.getLastName();
        photo = person.getPhoto();
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "AuthorData{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", id=" + id +
                ", photo='" + photo + '\'' +
                '}';
    }
}
