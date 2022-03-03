package howdo.vaccine.repository;

import howdo.vaccine.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> getUserByPpsNumber(String ppsNumber);

    @Query("SELECT COUNT(id) FROM User")
    int userTotal();


}
