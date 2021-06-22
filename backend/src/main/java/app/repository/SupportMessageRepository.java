package app.repository;

import app.model.SupportMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SupportMessageRepository extends JpaRepository<SupportMessage,Long> {

    @Query("from SupportMessage sm where sm.order.id = :id")
    List<SupportMessage> findByOrderId(@Param("id") long id);




}
