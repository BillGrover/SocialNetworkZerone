package app.dto.dialogs;

import app.dto.messages.MessageDto;
import app.dto.profile.PersonDto;
import app.model.Message;
import app.model.Person;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DialogDto {
    private int id;

    @JsonProperty("groupDialog")
    private boolean groupDialog = false;

    @JsonProperty("unread_count")
    private int unreadCount;

    @JsonProperty("last_message")
    private MessageDto lastMessage;

    private PersonDto[] persons;

    public PersonDto getState() {
        return state;
    }

    private PersonDto state;

    public DialogDto(int id) {
        this.id = id;
    }

    public DialogDto(int id, int unreadCount, Message lastMessage, Person currentPerson, Person interlocutor) {
        this.id = id;
        this.unreadCount = unreadCount;
        this.lastMessage = new MessageDto(lastMessage, (int) currentPerson.getId());
        this.persons = new PersonDto[]{
                new PersonDto(currentPerson),
                new PersonDto(interlocutor)
        };
        this.state =  new PersonDto(currentPerson);
    }

    public int getId() {
        return id;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public MessageDto getLastMessage() {
        return lastMessage;
    }

    public PersonDto[] getPersons() {
        return persons;
    }



    public boolean isGroupDialog() {
        return groupDialog;
    }
}
