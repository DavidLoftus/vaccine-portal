package howdo.vaccine.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class VaccinationCentre {

    @Id
    private Long id;

    @OneToMany(mappedBy = "location")
    List<Appointment> appointments;

}
