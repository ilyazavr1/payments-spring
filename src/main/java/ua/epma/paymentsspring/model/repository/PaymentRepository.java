package ua.epma.paymentsspring.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.epma.paymentsspring.model.entity.Card;
import ua.epma.paymentsspring.model.entity.Payment;
import ua.epma.paymentsspring.model.entity.User;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment getPaymentById(Long id);

    Page<Payment> findPaymentsByUserId(User user, Pageable pageable);

    List<Payment> getPaymentByUserId(User user);


}
