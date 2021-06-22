package app.dto.messages;

import app.dto.profile.PersonDto;
import app.model.Message;
import app.model.enums.MessageReadStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageDto {
    private long id;
    private long time;

    @JsonProperty("author_id")
    private long authorId;

    @JsonProperty("author")
    private PersonDto author;

    @JsonProperty("recipient_id")
    private long recipientId;

    @JsonProperty("recipient")
    private PersonDto recipient;

    @JsonProperty("message_text")
    private String messageText;

    @JsonProperty("read_status")
    private MessageReadStatus messageReadStatus;

    @JsonProperty("isSentByMe")
    private boolean isSentByMe;

    public MessageDto(Message message, int currentPersonId) {
        this.id = message.getId();
        this.time = message.getTime().getTime();
        this.author = new PersonDto(message.getAuthor());
        this.recipient = new PersonDto(message.getRecipient());
        this.messageText = message.getText();
        this.messageReadStatus = message.getMessageReadStatus();
        this.authorId = message.getAuthor().getId();
        this.recipientId = message.getRecipient().getId();
        this.isSentByMe = message.getAuthor().getId() == currentPersonId;
    }

    public long getId() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public PersonDto getAuthor() {
        return author;
    }

    public PersonDto getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public MessageReadStatus getMessageReadStatus() {
        return messageReadStatus;
    }

    public long getAuthorId() {
        return authorId;
    }

    public long getRecipientId() {
        return recipientId;
    }

    public boolean isSentByMe() {
        return isSentByMe;
    }


}
