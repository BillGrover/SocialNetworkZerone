package app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "countries")
public class Country implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @JsonProperty("title")
    @Column(nullable = false)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "country")
    private List<City> cities;

    @JsonIgnore
    @OneToMany(mappedBy = "country")
    private List<Person> countryPersons;

    public Country() {
    }

    public Country(long id, String name) {
        this.id = id;
        this.name = name;
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

    public List<Person> getCountryPersons() {
        return countryPersons;
    }

    public void setCountryPersons(List<Person> countryPersons) {
        this.countryPersons = countryPersons;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }
}
