package ua.epma.paymentsspring.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.epma.paymentsspring.model.entity.Card;
import ua.epma.paymentsspring.model.entity.User;
import ua.epma.paymentsspring.model.service.CardService;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {


    Card findByNumber(String number);

    Card findByNumberAndUserId(String number, User user);



    List<Card> findByUserId(User user);



}
