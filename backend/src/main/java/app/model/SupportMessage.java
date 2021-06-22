package app.model;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "support_message")
public class SupportMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Date date;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private SupportOrder order;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    private String text;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public SupportOrder getOrder() {
        return order;
    }

    public void setOrder(SupportOrder order) {
        this.order = order;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
