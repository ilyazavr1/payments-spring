package ua.epma.paymentsspring.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ua.epma.paymentsspring.model.entity.Payment;
import ua.epma.paymentsspring.model.entity.User;


public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment getPaymentById(Long id);

    Page<Payment> findPaymentsByUserIdOrUserDestinationId(User user1, User user2, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE Payment p SET p.balance = :senderBalance, p.balanceDestination = :destBalance WHERE p.cardSenderId.id = :senderCardId AND p.cardDestinationId.id = :destCardId AND p.isSend=false")
    void updatePreparedPaymentsByCardIds(@Param("senderCardId") Long senderCardId, @Param("senderBalance") int senderBalance,
                                         @Param("destCardId") Long destCardId, @Param("destBalance") int destBalance);


}
