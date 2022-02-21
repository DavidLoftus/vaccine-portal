package howdo.vaccine.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class ForumPost {

    @Id
    private Long id;

    @ManyToOne
    ForumThread thread;

    private Date dateCreated;

    @ManyToOne
    private User author;

    @Lob
    private String content;

}
