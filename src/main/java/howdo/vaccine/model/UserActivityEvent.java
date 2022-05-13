package howdo.vaccine.model;

import org.hibernate.annotations.ColumnTransformer;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
public class UserActivityEvent {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    @Column
    @ColumnTransformer(
            read="AES_DECRYPT(UNHEX(description), UNHEX(SHA2('secret', 512)))",
            write="HEX(AES_ENCRYPT(?, UNHEX(SHA2('secret', 512))))"
    )
    private String description;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @OrderColumn
    private Date date;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
