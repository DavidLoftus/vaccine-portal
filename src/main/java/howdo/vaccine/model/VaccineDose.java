package howdo.vaccine.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class VaccineDose {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade= CascadeType.ALL)
    private User user;

    private Date date;

    private Integer dose;

    private String vaccineType;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getDose() {
        return dose;
    }

    public void setDose(Integer dose) {
        this.dose = dose;
    }

    public String getVaccineType() {
        return vaccineType;
    }

    public void setVaccineType(String vaccineType) {
        this.vaccineType = vaccineType;
    }
}
