package app.repository;

import app.model.FriendshipStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipStatusRepository extends CrudRepository<FriendshipStatus, Long> {

}
