package howdo.vaccine.repository;
import howdo.vaccine.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface AppointmentRepository extends JpaRepository<Appointment, Long>{

    @Query("SELECT COUNT(id) FROM Appointment")
    int appointmentTotal();

}
