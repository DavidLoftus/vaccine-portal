package howdo.vaccine.repository;

import howdo.vaccine.model.VaccinationCentre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaccineCentreRepository extends JpaRepository<VaccinationCentre, Long> {}
