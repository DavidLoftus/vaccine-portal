package howdo.vaccine.model;

import org.hibernate.annotations.ColumnTransformer;

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

    @ColumnTransformer(
            read="AES_DECRYPT(UNHEX(vaccine_type), UNHEX(SHA2('secret', 512)))",
            write="HEX(AES_ENCRYPT(?, UNHEX(SHA2('secret', 512))))"
    )
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
