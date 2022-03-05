package howdo.vaccine.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.sql.Date;

@Entity
public class VaccineDose {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    private Date date;

    private Integer dose;

    private String vaccineType;
}
