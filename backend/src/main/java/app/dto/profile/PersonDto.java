package app.dto.profile;

import app.dto.location.CityDto;
import app.dto.location.CountryDto;
import app.model.Person;
import app.model.enums.BlockStatus;
import com.fasterxml.jackson.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({"id", "firstName", "lastName", "registrationDate", "birthDate", "email", "phone",
        "photo", "about", "about","fullName"})
@ApiModel
public class PersonDto {

    @ApiModelProperty(value = "id", example = "1")
    private long id;

    @JsonProperty("first_name")
    @ApiModelProperty(value = "first_name", example = "Петр")
    private String firstName;

    @JsonProperty("last_name")
    @ApiModelProperty(value = "last_name", example = "Петрович")
    private String lastName;

    @JsonProperty("reg_date")
    @ApiModelProperty(value = "reg_date", example = "1559751301818")
    private long registrationDate;

    @JsonProperty("birth_date")
    @ApiModelProperty(value = "birth_date", example = "1559751301818")
    private Date birthDate;

    @ApiModelProperty(value = "email", example = "petr@mail.ru")
    private String email;

    @ApiModelProperty(value = "phone", example = "89100000000")
    private String phone;

    @ApiModelProperty(value = "photo", example = "https://...../photos/image123.jpg")
    private String photo;

    @ApiModelProperty(value = "about", example = "Родился в небольшой, но честной семье")
    private String about;

    private CityDto city;

    private CountryDto country;

    private String fullName ;

    @JsonProperty("messages_permission")
    @ApiModelProperty(value = "messages_permission", example = "ALL")
    private String messagePermission;

    @JsonProperty("last_online_time")
    @ApiModelProperty(value = "last_online_time", example = "1559751301818")
    private Date lastOnlineTime;

    @JsonProperty("is_blocked")
    @ApiModelProperty(value = "is_blocked", example = "false")
    private boolean block;

    @ApiModelProperty(value = "token", example = "1q2e3e3r4t5")
    private String token;


    public PersonDto(Person person) {
        firstName = person.getFirstName();
        lastName = person.getLastName();
        id = person.getId();
        registrationDate = (person.getRegistrationDate() != null)
                ? person.getRegistrationDate().getTime()
                : 0;
        birthDate = (person.getBirthDate() != null)
                ? person.getBirthDate()
                : null;
        email = person.getEmail();
        phone = (person.getPhone() != null)
                ? person.getPhone()
                : null;
        photo = person.getPhoto();
        about = (person.getAbout() != null)
                ? person.getAbout()
                : null;
        city = (person.getCity() != null)
                ? new CityDto(person.getCity().getId(), person.getCity().getName())
                : new CityDto();
        country = (person.getCountry() != null)
                ? new CountryDto(person.getCountry().getId(), person.getCountry().getName())
                : new CountryDto();
        messagePermission = (person.getMessagePermission() != null)
                ? person.getMessagePermission().name()
                : null;
        lastOnlineTime = (person.getLastOnlineTime() != null)
                ? person.getLastOnlineTime()
                : null;
        block = person.getBlock() == BlockStatus.BLOCKED;
        fullName = person.getLastName() + " " + person.getFirstName();
    }

    public PersonDto(Person person, String token)   {
        this(person);
        this.token = token;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(long registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public CityDto getCity() {
        return city;
    }

    public void setCity(CityDto city) {
        this.city = city;
    }

    public CountryDto getCountry() {
        return country;
    }

    public void setCountry(CountryDto country) {
        this.country = country;
    }

    public String getMessagePermission() {
        return messagePermission;
    }

    public void setMessagePermission(String messagePermission) {
        this.messagePermission = messagePermission;
    }

    public Date getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(Date lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }

    public boolean isBlocked() {
        return block;
    }

    public void setBlocked(boolean blocked) {
        block = blocked;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "PersonDto{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", registrationDate=" + registrationDate +
                ", birthDate=" + birthDate +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", photo='" + photo + '\'' +
                ", about='" + about + '\'' +
                ", city=" + city +
                ", country=" + country +
                ", messagePermission='" + messagePermission + '\'' +
                ", lastOnlineTime=" + lastOnlineTime +
                ", block=" + block +
                ", token='" + token + '\'' +
                '}';
    }
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}