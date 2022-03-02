package howdo.vaccine.repository;

import howdo.vaccine.model.VaccineDose;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaccineDoseRepository extends JpaRepository<VaccineDose, Long> {
}
