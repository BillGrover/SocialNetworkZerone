package app.model;

import app.model.enums.AdminRole;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "admins")
public class Admin implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String email;

    private String name;

    private String password;

    @Enumerated(EnumType.STRING)
    private AdminRole status;

    @Column(name = "img_url")
    private String imageUrl;

    public Admin() {
    }

    public Admin(Person person, AdminRole status) {
        email = person.getEmail();
        name = String.format("%s %s", person.getFirstName(), person.getLastName());
        password = person.getPassword();
        this.status = status;
        imageUrl = person.getPhoto();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AdminRole getStatus() {
        return status;
    }

    public void setStatus(AdminRole status) {
        this.status = status;
    }

}
