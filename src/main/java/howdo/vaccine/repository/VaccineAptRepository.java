package howdo.vaccine.repository;

import howdo.vaccine.model.Appointment;
import howdo.vaccine.model.VaccinationCentre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface VaccineAptRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByAppointmentTime(Date date);
    List<Appointment> findByLocation(VaccinationCentre location);
}
