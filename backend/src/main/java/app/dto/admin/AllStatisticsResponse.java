package app.dto.admin;

public class AllStatisticsResponse {

    private long persons;
    private long posts;
    private long comments;
    private long likes;


    public long getPersons() {
        return persons;
    }

    public void setPersons(long persons) {
        this.persons = persons;
    }

    public long getPosts() {
        return posts;
    }

    public void setPosts(long posts) {
        this.posts = posts;
    }

    public long getComments() {
        return comments;
    }

    public void setComments(long comments) {
        this.comments = comments;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }
}
