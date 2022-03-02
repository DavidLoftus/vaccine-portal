package howdo.vaccine.repository;

import howdo.vaccine.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaccineAptRepository extends JpaRepository<Appointment, Long> {
}
