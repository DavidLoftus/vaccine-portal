package howdo.vaccine.repository;

import howdo.vaccine.model.ForumPost;
import howdo.vaccine.model.ForumThread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ForumPostRepository extends JpaRepository<ForumPost, Long> {

    @Query("SELECT COUNT(id) FROM ForumPost")
    int forumPostTotal();

}
