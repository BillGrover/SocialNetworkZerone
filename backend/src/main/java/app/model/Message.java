package app.model;

import app.model.enums.MessageReadStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "messages")
public class Message implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Date time;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Person author;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private Person recipient;

    private String text;

    @Enumerated(EnumType.STRING)
    private MessageReadStatus messageReadStatus;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "dialog_id")
    private Dialog dialog;


    // GETTERS & SETTERS

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }

    public Person getRecipient() {
        return recipient;
    }

    public void setRecipient(Person recipient) {
        this.recipient = recipient;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MessageReadStatus getMessageReadStatus() {
        return messageReadStatus;
    }

    public void setMessageReadStatus(MessageReadStatus messageReadStatus) {
        this.messageReadStatus = messageReadStatus;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }
}
