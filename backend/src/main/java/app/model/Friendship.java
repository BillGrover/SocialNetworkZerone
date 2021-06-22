package app.model;

import javax.persistence.*;

@Entity
@Table(name = "friendship")
public class Friendship {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "friendship_status_id", referencedColumnName = "id")
    private FriendshipStatus status;

    @OneToOne
    @JoinColumn(name = "src_person_id", referencedColumnName = "id")
    private Person srcPerson;

    @OneToOne
    @JoinColumn(name = "dst_person_id", referencedColumnName = "id")
    private Person dstPerson;


    // GETTERS & SETTERS

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public FriendshipStatus getStatus() {
        return status;
    }

    public void setStatus(FriendshipStatus status) {
        this.status = status;
    }

    public Person getSrcPerson() {
        return srcPerson;
    }

    public void setSrcPerson(Person srcPerson) {
        this.srcPerson = srcPerson;
    }

    public Person getDstPerson() {
        return dstPerson;
    }

    public void setDstPerson(Person dstPerson) {
        this.dstPerson = dstPerson;
    }

}
