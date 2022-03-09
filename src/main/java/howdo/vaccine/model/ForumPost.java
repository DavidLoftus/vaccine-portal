package howdo.vaccine.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ForumPost {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    ForumThread thread;

    private Date dateCreated;

    @ManyToOne
    private User author;

    @Lob
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ForumThread getThread() {
        return thread;
    }

    public void setThread(ForumThread thread) {
        this.thread = thread;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
