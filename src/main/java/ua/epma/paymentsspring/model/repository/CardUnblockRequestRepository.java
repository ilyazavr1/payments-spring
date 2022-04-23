package ua.epma.paymentsspring.model.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ua.epma.paymentsspring.model.entity.Card;
import ua.epma.paymentsspring.model.entity.CardUnblockRequest;

import java.util.List;

public interface CardUnblockRequestRepository extends JpaRepository<CardUnblockRequest, Long> {

    List<CardUnblockRequest> findAllByOrderByIdDesc();

    CardUnblockRequest findTopByCardIdOrderByIdDesc(Card card);


}
