package howdo.vaccine.repository;


import howdo.vaccine.model.User;
import howdo.vaccine.model.UserActivityEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserActivityEventRepository extends JpaRepository<UserActivityEvent, Long> {
    List<UserActivityEvent> getByUser(User user);
}
