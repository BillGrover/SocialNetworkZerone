package app.dto.main;

public class PaginationSettings {

    private final int offset;
    private final int perPage;


    public PaginationSettings(int offset, int perPage) {
        this.offset = offset;
        this.perPage = perPage;
    }

    public int getOffset() {
        return offset;
    }

    public int getPerPage() {
        return perPage;
    }

}
