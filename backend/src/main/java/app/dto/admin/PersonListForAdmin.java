package app.dto.admin;

import java.util.List;

public class PersonListForAdmin {

    List<PersonForAdminDto> data;

    public PersonListForAdmin(List<PersonForAdminDto> data) {
        this.data = data;
    }

    public List<PersonForAdminDto> getData() {
        return data;
    }

}
