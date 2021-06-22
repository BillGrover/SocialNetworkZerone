package app.repository;

import app.model.Friendship;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendshipRepository extends CrudRepository<Friendship, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM  friendship " +
            "where dst_person_id = ?1 and src_person_id=?2")
    Optional<Friendship> findAllByDstPersonAndSrcPerson(Long dstId, Long srcId);

    @Query(nativeQuery = true, value = "SELECT count(*) FROM  friendship as a " +
            "left join friendship_status as b on a.friendship_status_id = b.id " +
            "where b.friendship_code ='FRIEND' and dst_person_id = ?1 and src_person_id=?2")
    int countFriendAndFriend(Long dstId, Long srcId);


}
