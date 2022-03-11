package howdo.vaccine.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Appointment {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm", iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime appointmentTime;

    @ManyToOne
    private VaccinationCentre location;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
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
