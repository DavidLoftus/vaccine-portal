package howdo.vaccine.repository;

import howdo.vaccine.model.VaccinationCentre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VaccineCentreRepository extends JpaRepository<VaccinationCentre, Long> {}
