package app.model;

import app.model.enums.FriendshipCode;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "friendship_status")
public class FriendshipStatus {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    private Date time;

    private String name;

    @Enumerated(EnumType.STRING)
    private FriendshipCode friendshipCode;

    @OneToOne(mappedBy = "status")
    private Friendship friendship;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FriendshipCode getFriendshipCode() {
        return friendshipCode;
    }

    public void setFriendshipCode(FriendshipCode friendshipCode) {
        this.friendshipCode = friendshipCode;
    }

    public Friendship getFriendship() {
        return friendship;
    }

    public void setFriendship(Friendship friendship) {
        this.friendship = friendship;
    }
}
