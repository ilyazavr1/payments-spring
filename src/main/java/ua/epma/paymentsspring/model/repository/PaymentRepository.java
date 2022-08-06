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
    @Query("update Payment p set         p.balance = (case when p.cardSenderId.id = :senderCardId then :senderBalance\n" +
            "                                              when p.cardSenderId.id = :destCardId then :destBalance else p.balance end),\n" +
            "                 p.balanceDestination = (case when p.cardDestinationId.id = :senderCardId then :senderBalance\n" +
            "                                               when p.cardDestinationId.id = :destCardId then :destBalance else p.balanceDestination end) WHERE p.isSend= false")
    void updatePreparedPaymentsByCardIds(@Param("senderCardId") Long senderCardId, @Param("senderBalance") int senderBalance,
                                         @Param("destCardId") Long destCardId, @Param("destBalance") int destBalance);



    @Transactional
    @Modifying
    @Query("update Payment p set\n" +
            "                 p.balance = (case when p.cardSenderId.id = :senderCardId then :money else p.balance end),\n" +
            "                 p.balanceDestination = (case when p.cardDestinationId.id = :senderCardId then :money else p.balanceDestination end)" +
            " WHERE p.isSend = false")
    void updatePreparedPayments(@Param("senderCardId") Long senderCardId, @Param("money") int money);
}
