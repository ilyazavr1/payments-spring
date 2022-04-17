package ua.epma.paymentsspring.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.epma.paymentsspring.model.entity.Card;
import ua.epma.paymentsspring.model.entity.User;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {


    Card findByNumber(String number);

    List<Card> findByUserId(User user);


}
