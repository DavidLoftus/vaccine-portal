package howdo.vaccine.repository;

import howdo.vaccine.model.ForumThread;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumThreadRepository extends JpaRepository<ForumThread, Long> {
}
