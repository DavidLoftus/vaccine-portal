package howdo.vaccine.repository;

import howdo.vaccine.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface VaccineAptRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDate(Date date);
}
