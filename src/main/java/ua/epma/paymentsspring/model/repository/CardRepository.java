package ua.epma.paymentsspring.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.epma.paymentsspring.model.entity.Card;
import ua.epma.paymentsspring.model.entity.User;

import java.util.List;


public interface CardRepository extends JpaRepository<Card, Long> {


    Card findByNumber(String number);

    Page<Card> findCardsByUserId(User user, Pageable pageable);

    Card findByNumberAndUserId(String number, User user);

    List<Card> findByUserId(User user);



    @Query("SELECT c FROM Card c where c.userId.id = :id")
    List<Card> findByUserId(@Param(value="id") Long id);



}
