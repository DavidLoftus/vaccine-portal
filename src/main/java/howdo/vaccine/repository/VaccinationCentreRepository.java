package howdo.vaccine.repository;
import howdo.vaccine.model.VaccinationCentre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VaccinationCentreRepository extends JpaRepository<VaccinationCentre, Long>{

    @Query("SELECT COUNT(id) FROM VaccinationCentre ")
    int vaccinationCentreTotal();

}
