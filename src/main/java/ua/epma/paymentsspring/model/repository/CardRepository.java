package ua.epma.paymentsspring.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.epma.paymentsspring.model.entity.Card;
import ua.epma.paymentsspring.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {


    Card findByNumber(String number);

    Optional<Card> findById(Long id);

    Card findByNumberAndUserId(String number, User user);

    List<Card> findByUserId(User user);

    List<Card> findCardByUnderConsiderationTrue();


  /*  Page<Card> findAllBy(Pageable pageable);*/


    // @Query("SELECT u FROM User u where u.role.roleEnum = 'CLIENT'")
    @Query("SELECT c FROM Card c where c.userId.id = :id")
    List<Card> findByUserId(@Param(value="id") Long id);



}
