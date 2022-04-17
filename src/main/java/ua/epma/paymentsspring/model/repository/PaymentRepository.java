package ua.epma.paymentsspring.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.epma.paymentsspring.model.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {






}
