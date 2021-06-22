package app.repository;

import app.exceptions.BadRequestException;
import app.model.SupportOrder;
import app.model.enums.SupportOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupportOrderRepository extends JpaRepository<SupportOrder, Long> {

    Optional<SupportOrder> findByEmailAndStatusNot(String email, SupportOrderStatus status);

    Optional<SupportOrder>findById(long id) throws BadRequestException;

}
