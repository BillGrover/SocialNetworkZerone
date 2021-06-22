package app.dto.admin;

import app.model.Admin;


public class AdminLoginResponse {

    private String name;
    private String imageUrl;
    private String role;
    private String token;


    public AdminLoginResponse(Admin admin, String token) {
        name = admin.getName();
        role = admin.getStatus().toString();
        imageUrl = admin.getImageUrl();
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
