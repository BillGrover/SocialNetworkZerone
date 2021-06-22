package app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "dialogs")
public class Dialog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "dialog_starter_id")
    private int dialogStarterId;

    @Column(name = "dialog_target_id")
    private int dialogTargetId;

    @Column(name = "unread_count")
    private int unreadCount;

    @JsonIgnore
    @OneToMany(mappedBy = "dialog")
    private List<Message> messages;

    public Dialog() {
    }

    public Dialog(int dialogStarterId, int dialogTargetId) {
        this.dialogStarterId = dialogStarterId;
        this.dialogTargetId = dialogTargetId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public int getDialogStarterId() {
        return dialogStarterId;
    }

    public void setDialogStarterId(int dialogStarterId) {
        this.dialogStarterId = dialogStarterId;
    }

    public int getDialogTargetId() {
        return dialogTargetId;
    }

    public void setDialogTargetId(int dialogTargetId) {
        this.dialogTargetId = dialogTargetId;
    }
}
