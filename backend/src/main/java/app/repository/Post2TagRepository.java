package app.repository;

import app.model.Post;
import app.model.PostToTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository for working with "PostToTag".
 *
 * @author Surkov Aleksey (stibium128@gmail.com)
 */
public interface Post2TagRepository extends JpaRepository<PostToTag, Integer> {

    @Transactional
    @Modifying
    void deleteAllByPost(Post post);

}
