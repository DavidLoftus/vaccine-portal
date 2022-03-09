package howdo.vaccine.repository;

import howdo.vaccine.model.VaccineDose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface VaccineDoseRepository extends JpaRepository<VaccineDose, Long> {

    @Query("SELECT COUNT(id) FROM VaccineDose ")
    int doseTotal();


}
