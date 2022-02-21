package howdo.vaccine.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
public class ForumThread {
    @Id
    private Long id;

    @ManyToOne
    private User author;

    @NotBlank
    private String title;

    @OneToMany(mappedBy = "thread")
    @OrderBy(value = "dateCreated")
    private List<ForumPost> posts;
}
