package howdo.vaccine.repository;

import howdo.vaccine.model.Appointment;
import howdo.vaccine.model.VaccinationCentre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface VaccineAptRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByAppointmentTime(Date date);
    List<Appointment> findByLocation(VaccinationCentre location);

    @Query(value = "SELECT a FROM Appointment a WHERE a.location = :centre AND :minDate <= a.appointmentTime AND a.appointmentTime < :maxDate")
    List<Appointment> findAppointmentsOnDay(@Param("centre") VaccinationCentre centre, @Param("minDate") LocalDateTime minDate, @Param("maxDate") LocalDateTime maxDate);
}
