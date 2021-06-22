package app.dto.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel
public class PersonUpdateRequest {

    @JsonProperty("first_name")
    @ApiModelProperty(value = "first_name", example = "Петр")
    private String firstName;

    @JsonProperty("last_name")
    @ApiModelProperty(value = "last_name", example = "Петрович")
    private String lastName;

    @JsonProperty("birth_date")
    @ApiModelProperty(value = "birth_date", example = "1559751301818")
    private Date birthDate;

    @ApiModelProperty(value = "phone", example = "89100000000")
    private String phone;

    @JsonProperty("photo_id")
    @ApiModelProperty(value = "photo_id", example = "o1doj1d91j1d01d-1d1f")
    private String photoId;

    @ApiModelProperty(value = "about", example = "Родился в небольшой, но честной семье")
    private String about;

    @JsonProperty("city")
    @ApiModelProperty(value = "town_id", example = "123")
    private long cityId;

    @JsonProperty("country")
    @ApiModelProperty(value = "country_id", example = "33")
    private long countryId;

    @JsonProperty("messages_permission")
    @ApiModelProperty(value = "messages_permission", example = "ALL")
    private String messagesPermission;


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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public String getMessagesPermission() {
        return messagesPermission;
    }

    public void setMessagesPermission(String messagesPermission) {
        this.messagesPermission = messagesPermission;
    }

    @Override
    public String toString() {
        return "PersonUpdateRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", phone='" + phone + '\'' +
                ", photoId='" + photoId + '\'' +
                ", about='" + about + '\'' +
                ", cityId=" + cityId +
                ", countryId=" + countryId +
                ", messagesPermission='" + messagesPermission + '\'' +
                '}';
    }
}
