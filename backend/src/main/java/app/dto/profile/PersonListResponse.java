package app.dto.profile;

import app.dto.main.PaginationResponse;

import java.util.List;

public class PersonListResponse extends PaginationResponse {

    List<PersonDto> data;


    public List<PersonDto> getData() {
        return data;
    }

    public void setData(List<PersonDto> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PersonListResponse{" +
                "data=" + data +
                '}';
    }
}
