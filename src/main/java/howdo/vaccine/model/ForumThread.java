package howdo.vaccine.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
public class ForumThread {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User author;

    @NotBlank
    private String title;

    @OneToMany(mappedBy = "thread")
    @OrderBy(value = "dateCreated")
    private List<ForumPost> posts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ForumPost> getPosts() {
        return posts;
    }

    public void setPosts(List<ForumPost> posts) {
        this.posts = posts;
    }
}
