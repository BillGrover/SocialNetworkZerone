package app.repository;

import app.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByPostAndPerson(Post post, Person person);

    Optional<PostLike> findByPostCommentIdAndPerson(PostComment postComment, Person person);

    Iterable<PostLike> findAllByPostCommentId(PostComment postComment);

}
