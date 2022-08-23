package ua.epma.paymentsspring.service;


import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.epma.paymentsspring.exception.*;
import ua.epma.paymentsspring.model.entity.Card;
import ua.epma.paymentsspring.model.entity.CardUnblockRequest;
import ua.epma.paymentsspring.model.entity.Role;
import ua.epma.paymentsspring.model.entity.User;
import ua.epma.paymentsspring.model.repository.CardRepository;
import ua.epma.paymentsspring.model.repository.CardUnblockRequestRepository;
import ua.epma.paymentsspring.model.repository.PaymentRepository;
import ua.epma.paymentsspring.model.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
@Service
public class CardService {

    private CardRepository cardRepository;
    private UserRepository userRepository;
    private PaymentRepository paymentRepository;
    private CardUnblockRequestRepository cardUnblockRequestRepository;


    public Card getCardById(Long id) {
        Optional<Card> card = cardRepository.findById(id);
        return card.orElse(null);
    }

    public Page<Card> getCardPagination(Pageable pageable) {
        return cardRepository.findCardsByUserId(userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()), pageable);
    }

    public List<Card> getCardListByCurrentUser() {
        return cardRepository.findByUserId(userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    public List<Card> getCardListByUserEmail(String email) {
        return cardRepository.findByUserId(userRepository.findByEmail(email));
    }

    public Card getCardByCurrentUserByNumber(String number) throws InvalidCardNumberException {

        Card card = cardRepository.findByNumberAndUserId(number, userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));

        if (card == null) {
            throw new InvalidCardNumberException();
        }
        return card;
    }

    public List<Card> getCardListByUserId(Long id) {
        return cardRepository.findByUserId(id);
    }

    public List<CardUnblockRequest> getCardUnblockRequest() {
        return cardUnblockRequestRepository.findAllByOrderByIdDesc();
    }


    /**
     * Adds the money amount to the card.
     *
     * @param number Card number
     * @param money  money to add to the Card
     * @throws BlockedCardException        if Card is blocked
     * @throws InvalidMoneyAmountException if wrong card number
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public void updateCardWithMoney(final String number, final String money) throws BlockedCardException, InvalidMoneyAmountException {
        Card card;

        try {
            card = getCardByCurrentUserByNumber(number);
        } catch (InvalidCardNumberException e) {
            return;
        }
        if (card.isBlocked()) {
            throw new BlockedCardException();
        }

        card.setMoney(card.getMoney().add(validateAndGetMoney(money)));

        cardRepository.save(card);

        paymentRepository.updatePreparedPayments(card.getId(), card.getMoney());
    }

    /**
     * Validates card name.
     * Generates card number, if it exists interrupts method.
     * Creates new card signed to User in current session.
     * Saves new card to database.
     *
     * @param name card name
     * @throws InvalidCardName if name is invalid
     */
    public Card createCardAndAddToUser(String name, String email) throws InvalidCardName {
        if (name.length() > 30 || name.length() < 3) {
            throw new InvalidCardName();
        }
        String number = generateCardNumber();
        if (cardRepository.findByNumber(number) != null) {
            return null;
        }

        Card card = Card.builder()
                .name(name)
                .number(number)
                .userId(userRepository.findByEmail(email))
                .money(BigDecimal.ZERO)
                .build();

        return cardRepository.save(card);
    }

    /**
     * Checks whether the card is under considered, if so, then updates the unlock requests.
     * Unblock card and sets under consideration to false.
     * Saves Card to database.
     *
     * @param number Card number
     * @throws InvalidCardNumberException if card number is wrong
     */
    public void unblockCardByNumber(String number) throws InvalidCardNumberException {
        Card card = cardRepository.findByNumber(number);
        if (card == null) {
            throw new InvalidCardNumberException();
        }

        if (card.isUnderConsideration()) {
            updateRequests(card);
        }

        card.setBlocked(false);
        card.setUnderConsideration(false);

        cardRepository.save(card);
    }

    /**
     * Method updates the status of the request by changing it to the processed.
     *
     * @param card unblocked Card for which the unlock request must be updated
     */
    private void updateRequests(Card card) {
        CardUnblockRequest request = cardUnblockRequestRepository.findTopByCardIdOrderByIdDesc(card);
        request.setProcessed(true);
        cardUnblockRequestRepository.save(request);
    }

    /**
     * If user entered right password than blocks card by number.
     *
     * @param number   card number
     * @param password user entered password
     * @throws InvalidCardNumberException    if card number is wrong
     * @throws AuthenticationFailedException is user entered wrong password
     */
    public void blockCardByNumber(String number, String password) throws InvalidCardNumberException, AuthenticationFailedException {
        Card card;

        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().contains(Role.RoleEnum.ACCOUNTANT.name())) {
            card = cardRepository.findByNumber(number);
        } else {
            card = getCardByCurrentUserByNumber(number);
        }
        if (card == null || card.isBlocked()) {
            return;
        }

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        if (!encoder.matches(password, user.getPassword())) {
            throw new AuthenticationFailedException();
        }

        card.setBlocked(true);
        cardRepository.save(card);
    }

    /**
     * Changes status of unblock request to processed.
     *
     * @param id request id
     */
    public void processedCardUnblockRequest(Long id) {
        CardUnblockRequest request = cardUnblockRequestRepository.getById(id);
        request.setProcessed(true);
        cardUnblockRequestRepository.save(request);
    }

    /**
     * Changes status of card to under consideration.
     * Creates card unblock request.
     *
     * @param number card number
     * @throws InvalidCardNumberException if card number is wrong
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public void makeCardUnblockRequest(String number) throws InvalidCardNumberException {
        Card card = cardRepository.findByNumber(number);
        if (card == null) {
            throw new InvalidCardNumberException();
        }

        card.setUnderConsideration(true);

        cardRepository.save(card);
        cardUnblockRequestRepository.save(CardUnblockRequest.builder().cardId(card).userId(card.getUserId()).isProcessed(false).build());
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public boolean transferMoney(Card cardFrom, Card cardTo, BigDecimal money) throws InvalidCardNumberException, BlockedCardException, InvalidBalanceOnCardException {

        if (!validateTransfer(cardFrom, cardTo, money)) {
            return false;
        }


        cardFrom.setMoney(cardFrom.getMoney().subtract(money));
        cardTo.setMoney(cardTo.getMoney().add(money));

        cardRepository.save(cardFrom);

        cardRepository.save(cardTo);
        return true;
    }

    public boolean validateTransfer(Card cardFrom, Card cardTo, BigDecimal money) throws InvalidCardNumberException, InvalidBalanceOnCardException, BlockedCardException {
        if (cardTo == null || cardFrom.getNumber().equals(cardTo.getNumber())) {
            throw new InvalidCardNumberException();
        }
        if (cardFrom.isBlocked() || cardTo.isBlocked()) {
            throw new BlockedCardException();
        }
        if ((cardFrom.getMoney().subtract(money)).compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidBalanceOnCardException();
        }

        return true;
    }

    public BigDecimal validateAndGetMoney(String money) throws InvalidMoneyAmountException {
        if (money.isEmpty() || !money.replaceFirst("^0*", "").matches("^[0-9]*\\.?[0-9]{0,2}$")) {
            throw new InvalidMoneyAmountException();
        }
        BigDecimal moneyBigDecimal = new BigDecimal(money);
        System.out.println(moneyBigDecimal);
        System.out.println(moneyBigDecimal.compareTo(BigDecimal.ZERO) <= 0 );
        System.out.println(moneyBigDecimal.compareTo(new BigDecimal(10000)) > 0);
        if (moneyBigDecimal.compareTo(BigDecimal.ZERO) <= 0 || moneyBigDecimal.compareTo(new BigDecimal(10000)) > 0) {
            throw new InvalidMoneyAmountException();
        }
        return moneyBigDecimal;
    }

    public String generateCardNumber() {
        long randomNum = ThreadLocalRandom.current().nextLong(1, 1_0000_0000_0000L);

        if (String.valueOf(randomNum).length() < 12) {
            return String.format("1234%012d", randomNum);
        }

        return "1234" + randomNum;
    }

}
