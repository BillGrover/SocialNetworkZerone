package app.dto.messages;

public class UnreadMessageCountDto {
    private int count;

    public UnreadMessageCountDto(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
