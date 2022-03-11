package howdo.vaccine.repository;

import howdo.vaccine.model.Appointment;
import howdo.vaccine.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> getUserByPpsNumber(String ppsNumber);

    @Query("SELECT COUNT(id) FROM User")
    int userTotal();

    @Query("SELECT COUNT(id) FROM User WHERE doses.size = 0")
    int zeroDosesTotal();

    @Query("SELECT COUNT(id) FROM User WHERE doses.size = 1")
    int oneDosesTotal();

    @Query("SELECT COUNT(id) FROM User WHERE doses.size = 2")
    int twoDosesTotal();

    @Query("SELECT firstName FROM User WHERE id = :id")
    String getUserFirstName(@Param("id") Long id);

    @Query("SELECT lastName FROM User WHERE id = :id")
    String getUserLastName(@Param("id") Long id);

    @Query("SELECT ppsNumber FROM User WHERE id = :id")
    String getPpsNumber(@Param("id") Long id);

    @Query("SELECT dateOfBirth FROM User WHERE id = :id")
    String getDateOfBirth(@Param("id") Long id);

    @Query("SELECT phoneNumber FROM User WHERE id = :id")
    String getPhoneNumber(@Param("id") Long id);

    @Query("SELECT emailAddress FROM User WHERE id = :id")
    String getEmailAddress(@Param("id") Long id);

    @Query("SELECT nationality FROM User WHERE id = :id")
    String getNationality(@Param("id") Long id);

    @Query("SELECT doses FROM User WHERE id = :id")
    String getDoses(@Param("id") Long id);

    @Query("SELECT appointments FROM User WHERE id = :id")
    List<Appointment> getAppointments(@Param("id") Long id);


}
