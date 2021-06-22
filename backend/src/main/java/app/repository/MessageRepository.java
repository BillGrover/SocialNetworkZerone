package app.repository;

import app.model.Message;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * Подсчёт количества непрочитанных сообщений в конкретном диалоге для конкретного юзера.
     */
    @Query(value = "SELECT COUNT(m) FROM Message m WHERE m.messageReadStatus = 'SENT' " +
            "AND m.recipient.id = :pid AND m.dialog.id = :did")
    int countUnread(@Param("pid") long currentPersonId, @Param("did") int currentDialogId);

    /**
     * Подсчёт количества непрочитанных сообщений во всех диалогах конкретного юзера.
     */
    @Query(value = "SELECT COUNT(m) FROM Message m WHERE m.messageReadStatus = 'SENT' " +
            "AND m.recipient.id = :pid")
    int countUnread(@Param("pid") long currentPersonId);

    /**
     * Выбор последнего сообщения в конкретном диалоге.
     */
    @Query(nativeQuery = true,
            value = "SELECT * FROM messages WHERE dialog_id = :id ORDER BY time DESC LIMIT 1")
    Message findLastMessage(@Param("id") int id);

    /**
     * Поиск всех сообщений в конкретном диалоге.
     */
    @Query(value = "FROM Message m WHERE m.dialog.id = :id")
    List<Message> findAllByDialogId(@Param("id") int id);

    /**
     * Удаление всех сообщений в диалоге
     * @param id id диалога
     */
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Message m WHERE m.dialog.id = :id")
    void removeByDialogId(@Param("id") int id);
}
