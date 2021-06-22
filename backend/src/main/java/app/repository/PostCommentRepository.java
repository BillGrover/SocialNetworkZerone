package app.repository;

import app.model.Post;
import app.model.PostComment;
import app.model.enums.DeleteStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for working with "PostComment".
 *
 * @author Surkov Aleksey (stibium128@gmail.com)
 */
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    /**
     * Find all comments on a post.
     *
     * @param post post on which need search.
     * @return List post comments.
     */
    List<PostComment> findPostCommentsByPost(Post post);

    List<PostComment> findAllByParentId(PostComment postComment);

    List<PostComment> findAllByDeleteStatus(DeleteStatus deleteStatus);

    Page<PostComment> findAllByPost(Post post, Pageable pageable);

    int countAllByPost(Post post);

}
