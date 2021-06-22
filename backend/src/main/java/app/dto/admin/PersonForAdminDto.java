package app.dto.admin;

import app.model.Person;
import app.model.enums.ApprovalStatus;
import app.model.enums.BlockStatus;

public class PersonForAdminDto {

    private final long id;
    private final String firstName;
    private final String lastName;
    private final ApprovalStatus approvalStatus;
    private final BlockStatus blockStatus;

    public PersonForAdminDto(Person person) {
        id = person.getId();
        firstName = person.getFirstName();
        lastName = person.getLastName();
        approvalStatus = person.getApproval();
        blockStatus = person.getBlock();
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    public BlockStatus getBlockStatus() {
        return blockStatus;
    }

}
