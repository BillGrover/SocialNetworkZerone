package app.model;

import app.model.enums.NotificationType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "notifications")
public class Notification implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private Date time;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person authorPerson;


    @ManyToOne
    @JoinColumn(name = "entity_id")
    private Person targetPerson;

    private String contact;

    @JoinColumn(name = "read_status")
    private String readStatus;
    // GETTERS & SETTERS


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Person getTargetPerson() {
        return targetPerson;
    }

    public void setTargetPerson(Person targetPerson) {
        this.targetPerson = targetPerson;
    }

    public Person getAuthorPerson() {
        return authorPerson;
    }

    public void setAuthorPerson(Person authorPerson) {
        this.authorPerson = authorPerson;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }
}
