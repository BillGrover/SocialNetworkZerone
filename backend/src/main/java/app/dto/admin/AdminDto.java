package app.dto.admin;

import app.model.Admin;

public class AdminDto {

    private long id;
    private String name;
    private String status;
    private String imageUrl;


    public AdminDto(Admin admin) {
        id = admin.getId();
        name = admin.getName();
        status = admin.getStatus().toString();
        imageUrl = admin.getImageUrl();
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
