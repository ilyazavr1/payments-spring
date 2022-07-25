package ua.epma.paymentsspring.test;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ua.epma.paymentsspring.model.dto.PaymentDto;
import ua.epma.paymentsspring.model.dto.UserDto;
import ua.epma.paymentsspring.model.entity.Card;
import ua.epma.paymentsspring.model.entity.User;
import ua.epma.paymentsspring.model.exception.*;
import ua.epma.paymentsspring.model.repository.CardRepository;
import ua.epma.paymentsspring.model.service.CardService;
import ua.epma.paymentsspring.model.service.PaymentService;
import ua.epma.paymentsspring.model.service.UserService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class DemoData implements ApplicationRunner {

    final UserService userService;
    final CardService cardService;
    final PaymentService paymentService;
    final CardRepository cardRepository;

    public DemoData(UserService userService, CardService cardService, PaymentService paymentService, CardRepository cardRepository) {
        this.userService = userService;
        this.cardService = cardService;
        this.paymentService = paymentService;
        this.cardRepository = cardRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        createUsers();
        createAddCardsToUsers();
        topUpAllCards();
        makeSentPayments();
        makePreparedPayments();
    }


    private void createUsers() throws UserAlreadyExistException {
        userService.registerUser(UserDto.builder().firstName("Vlad").lastName("Lizogub").patronymic("Olekseevich").email("vlad@gmail.com").password("Qwerty12345").build());
        userService.registerUser(UserDto.builder().firstName("Artem").lastName("Vinyk").patronymic("Nikitovich").email("artem@gmail.com").password("Qwerty12345").build());
        userService.registerUser(UserDto.builder().firstName("Nikita").lastName("Gamaunov").patronymic("Artomovich").email("nikita@gmail.com").password("Qwerty12345").build());
        userService.registerUser(UserDto.builder().firstName("Sergey").lastName("Masyna").patronymic("Anatoievich").email("sergey@gmail.com").password("Qwerty12345").build());
        userService.registerUser(UserDto.builder().firstName("Oleksandr").lastName("Masyna").patronymic("Sergeeyovich").email("oleksandr@gmail.com").password("Qwerty12345").build());
        userService.registerUser(UserDto.builder().firstName("Anaton").lastName("Krivenko").patronymic("Sergeeyovich").email("anaton@gmail.com").password("Qwerty12345").build());
        userService.registerUser(UserDto.builder().firstName("Yaroslav").lastName("Kolomiec").patronymic("Sergeeyovich").email("yaroslav@gmail.com").password("Qwerty12345").build());
    }

    private void createAddCardsToUsers() throws InvalidCardName {

        for (String email : userService.getAllUsers().stream().map(User::getEmail).collect(Collectors.toList())) {
            Pattern p = Pattern.compile("^[\\w]+[^@$]");
            Matcher m = p.matcher(email);
            if (m.find()) {
                cardService.createCardAndAddToUser(m.group() + "Card1", email);
                cardService.createCardAndAddToUser(m.group() + "Card2", email);
            }
        }

        cardService.createCardAndAddToUser("vladCard3", "vlad@gmail.com");
        cardService.createCardAndAddToUser("vladCard4", "vlad@gmail.com");
    }

    private void topUpAllCards() {
        List<Card> cardList = new ArrayList<>();
        for (String email : userService.getAllUsers().stream().map(User::getEmail).collect(Collectors.toList())) {
            int money = 1000;

            for (Card card : cardService.getCardListByUserEmail(email)) {
                card.setMoney(money);
                money += 100;
                cardList.add(card);
            }
        }
        cardRepository.saveAll(cardList);
    }


    private void makeSentPayments() throws InvalidBalanceOnCardException, InvalidCardNumberException, BlockedCardException {
        Card from = cardService.getCardListByUserEmail("vlad@gmail.com").get(0);
        Card to = cardService.getCardListByUserEmail("artem@gmail.com").get(0);
        PaymentDto paymentDto = PaymentDto.builder()
                .cardSenderNumber(from.getNumber())
                .cardDestinationNumber(to.getNumber())
                .money(100)
                .build();


        for (int i = 0; i < 3; i++) {
            paymentService.makePayment(paymentDto);
        }

    }

    private void makePreparedPayments() throws InvalidBalanceOnCardException, InvalidCardNumberException, BlockedCardException {
        Card from = cardService.getCardListByUserEmail("vlad@gmail.com").get(1);
        Card to = cardService.getCardListByUserEmail("nikita@gmail.com").get(0);
        PaymentDto paymentDto = PaymentDto.builder()
                .cardSenderNumber(from.getNumber())
                .cardDestinationNumber(to.getNumber())
                .money(100)
                .build();


        for (int i = 0; i < 3; i++) {
            paymentService.createPreparedPayment(paymentDto);
        }


    }
}