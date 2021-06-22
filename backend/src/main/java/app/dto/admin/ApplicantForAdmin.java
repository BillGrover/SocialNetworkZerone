package app.dto.admin;

import app.model.Person;

public class ApplicantForAdmin {

    private long id;
    private String name;
    private int age;
    private String country;
    private String city;
    private int posts;
    private int comments;
    private String imageUrl;

    public ApplicantForAdmin(Person person) {
        id = person.getId();
        name = String.format("%s %s", person.getFirstName(), person.getLastName());
        country = person.getCountry().getName();
        city = person.getCity().getName();
        posts = person.getPostList().size();
        comments = person.getComments().size();
        imageUrl = person.getPhoto();
        age = PersonAdminPanelDto.calculatingAge(person);
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
