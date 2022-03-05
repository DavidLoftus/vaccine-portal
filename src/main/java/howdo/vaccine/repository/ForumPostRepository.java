package howdo.vaccine.repository;

import howdo.vaccine.model.ForumPost;
import howdo.vaccine.model.ForumThread;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumPostRepository extends JpaRepository<ForumPost, Long> {
}
