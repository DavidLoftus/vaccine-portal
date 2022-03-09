package howdo.vaccine.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
public class Appointment {
    @Id
    private Long id;

    @NotBlank
    private Date appointmentTime;

    @ManyToOne
    private VaccinationCentre location;

    @ManyToOne(cascade= CascadeType.ALL)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Date appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public VaccinationCentre getLocation() {
        return location;
    }

    public void setLocation(VaccinationCentre location) {
        this.location = location;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
