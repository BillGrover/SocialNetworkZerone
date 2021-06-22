package app.repository;

import app.model.Notification;
import app.model.Person;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, Long> {

    List<Notification> findAllByTargetPersonAndReadStatusNotOrderByIdDesc(Person person, String rearStatus);

    int countByTargetPerson(Person person);

    @Modifying
    @Transactional
    void deleteById(long id);

}
