package app.repository;

import app.model.NotificationsSettings;
import app.model.Person;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NotificationSettingsRepository extends CrudRepository<NotificationsSettings, Long> {

    int countByPersonsAndType(Person person, String type);

    @Modifying
    @Transactional
    void deleteByPersonsAndType(Person person, String type);

    List<NotificationsSettings> findAllByPersons(Person person);

}
