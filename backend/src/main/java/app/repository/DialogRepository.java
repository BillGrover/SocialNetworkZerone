package app.repository;

import app.model.Dialog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DialogRepository extends JpaRepository<Dialog, Integer> {

    @Query(value = "FROM Dialog d WHERE d.dialogStarterId = :id OR d.dialogTargetId = :id")
    List<Dialog> findAll(@Param("id") int id);

    @Query(value = "SELECT COUNT(d) FROM Dialog d WHERE d.dialogStarterId = :id OR d.dialogTargetId = :id")
    int countByCurrentPerson(@Param("id") int id);

    @Query(value = "FROM Dialog d " +
            "WHERE (d.dialogStarterId = ?1 AND d.dialogTargetId = ?2) " +
            "OR (d.dialogStarterId = ?2 AND d.dialogTargetId = ?1)")
    Dialog findByBothPersonId(int firstPersonId, int secondPersonId);
}
