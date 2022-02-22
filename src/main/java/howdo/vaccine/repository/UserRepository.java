package howdo.vaccine.repository;

import howdo.vaccine.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> getUserByPpsNumber(String ppsNumber);
}
