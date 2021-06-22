package app.dto.admin;

import app.exceptions.BadRequestException;
import app.model.Person;
import app.model.enums.ApprovalStatus;
import app.model.enums.BlockStatus;
import app.service.MainService;

import java.time.Instant;
import java.time.LocalDate;
import java.util.TimeZone;

public class PersonAdminPanelDto {

    private long id;
    private String name;
    private Integer age;
    private String city;
    private String approval;
    private String block;
    private String imageUrl;


    public PersonAdminPanelDto(Person person, MainService mainService) throws BadRequestException {
        id = person.getId();
        if ((person.getBlock() == BlockStatus.TOTAL_BLOCKED || person.getApproval() == ApprovalStatus.DELETED)
                && (person.getConfirmationCode() != null)) {
            Person restorePerson = mainService.restorePersonInfo(person);
            setDataFromPerson(restorePerson);
        } else {
            setDataFromPerson(person);
        }
        block = person.getBlock().toString();
        imageUrl = person.getPhoto();
    }

    private void setDataFromPerson(Person person) {
        String fullName = "%s %s";
        name = String.format(fullName, person.getFirstName(), person.getLastName());
        age = person.getBirthDate() == null ? null : calculatingAge(person);
        city = person.getCity() == null ? null : person.getCity().getName();
        approval = person.getApproval().toString();
    }

    public static int calculatingAge(Person person) {
        int ageCalculate;
        LocalDate localDate = LocalDate.ofInstant(Instant.ofEpochMilli(person.getBirthDate().getTime()), TimeZone.getDefault().toZoneId());
        if (LocalDate.now().getDayOfYear() > localDate.getDayOfYear()) {
            ageCalculate = LocalDate.now().getYear() - localDate.getYear();
        } else {
            ageCalculate = (LocalDate.now().getYear() - localDate.getYear()) - 1;
        }
        return ageCalculate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
