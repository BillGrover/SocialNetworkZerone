package app.model;

import app.model.enums.SupportOrderStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "support_order")
public class SupportOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String email;
    private String title;
    private Date date;

    @Enumerated(EnumType.STRING)
    private SupportOrderStatus status;

    @OneToMany(mappedBy = "order")
    List<SupportMessage> orderList;


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public SupportOrderStatus getStatus() {
        return status;
    }

    public void setStatus(SupportOrderStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}