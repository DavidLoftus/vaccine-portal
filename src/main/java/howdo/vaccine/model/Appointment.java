package howdo.vaccine.model;

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

    @ManyToOne
    private User user;
}
