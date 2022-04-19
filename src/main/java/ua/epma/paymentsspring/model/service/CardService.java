package ua.epma.paymentsspring.model.service;


import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.epma.paymentsspring.model.dto.PaymentDto;
import ua.epma.paymentsspring.model.entity.Card;
import ua.epma.paymentsspring.model.entity.User;
import ua.epma.paymentsspring.model.excwption.*;
import ua.epma.paymentsspring.model.repository.CardRepository;
import ua.epma.paymentsspring.model.repository.UserRepository;


import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
@Service
public class CardService {

    private CardRepository cardRepository;
    private UserRepository userRepository;

    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.REPEATABLE_READ)
    public boolean transferMoney(Card cardFrom, Card cardTo, int money) throws InvalidCardNumberException, BlockedCardException, InvalidBalanceOnCardException {

        if (!validateTransfer(cardFrom, cardTo, money)) return false;

        cardFrom.setMoney(cardFrom.getMoney() - money);
        cardTo.setMoney(cardTo.getMoney() + money);

        cardRepository.save(cardFrom);
        cardRepository.save(cardTo);

        return true;
    }

    public boolean validateTransfer(Card cardFrom, Card cardTo, int money) throws InvalidCardNumberException, InvalidBalanceOnCardException, BlockedCardException {
        /*if (money <= 0 || money > 10000) throw new InvalidMoneyAmountException();*/

        if (cardTo == null || cardFrom.getNumber().equals(cardTo.getNumber())) throw new InvalidCardNumberException();
        if (cardFrom.isBlocked() || cardTo.isBlocked()) throw new BlockedCardException();
        if ((cardFrom.getMoney() - money) < 0) throw new InvalidBalanceOnCardException();

        return true;
    }

    public List<Card> getCardListByUser() {
        return cardRepository.findByUserId(userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    public Card getCardOfCurrentUserByNumber(String number) throws InvalidCardNumberException {
        Card card = cardRepository.findByNumberAndUserId(number, userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
        if (card == null) throw new InvalidCardNumberException();
        return card;
    }

    public void updateCardWithMoney(String number, String money) throws BlockedCardException, InvalidMoneyAmountException {
        Card card;
        try {
            card = getCardOfCurrentUserByNumber(number);
        } catch (InvalidCardNumberException e) {
            throw new RuntimeException();
        }
        if (card.isBlocked()) throw new BlockedCardException();

        card.setMoney(card.getMoney() + validateMoney(money));

        cardRepository.save(card);

    }

    public void createCard(String name) throws InvalidCardName {
        if (name.length() > 30 || name.length() < 3) throw new InvalidCardName();
        String number = generateCardNumber();
        if (cardRepository.findByNumber(number) != null) return;

        Card card = Card.builder()
                .name(name)
                .number(number)
                .userId(userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()))
                .build();

        cardRepository.save(card);
    }

    public void blockCardByNumber(String number, String password) throws InvalidCardNumberException, AuthenticationFailedException {
        Card card = getCardOfCurrentUserByNumber(number);
        if (card == null || card.isBlocked()) return;

        PasswordEncoder encoder = new BCryptPasswordEncoder();

        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        if (!encoder.matches(password, user.getPassword())) throw new AuthenticationFailedException();

        card.setBlocked(true);

        cardRepository.save(card);
    }


    public int validateMoney(String money) throws InvalidMoneyAmountException {
        if (money.isEmpty() || !money.replaceFirst("^0*", "").matches("^[0-9]{0,5}$"))
            throw new InvalidMoneyAmountException();
        int moneyInt = Integer.parseInt(money);
        if (moneyInt <= 0 || moneyInt > 10000) throw new InvalidMoneyAmountException();
        return moneyInt;
    }


    private static String generateCardNumber() {
        long randomNum = ThreadLocalRandom.current().nextLong(1, 1_0000_0000_0000L);

        if (String.valueOf(randomNum).length() < 12) {
            return String.format("1234%012d", randomNum);
        }

        return "1234" + randomNum;
    }

}
