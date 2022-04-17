package ua.epma.paymentsspring.model.service;


import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ua.epma.paymentsspring.model.entity.Card;
import ua.epma.paymentsspring.model.excwption.InvalidCardName;
import ua.epma.paymentsspring.model.repository.CardRepository;
import ua.epma.paymentsspring.model.repository.UserRepository;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
@Service
public class CardService {

    private CardRepository cardRepository;
    private UserRepository userRepository;



    public List<Card> getCardListByUser(){
        return cardRepository.findByUserId(userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    public void createCard(String name) throws InvalidCardName {
        if (name.length() > 30 || name.length() < 1) throw new InvalidCardName();
        String number = generateCardNumber();

        if (cardRepository.findByNumber(number) != null) return;

        Card card = Card.builder()
                .name(name)
                .number(number)
                .userId(userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()))
                .build();

        cardRepository.save(card);
    }


    private static String generateCardNumber() {
        long randomNum = ThreadLocalRandom.current().nextLong(1, 1_0000_0000_0000L);

        if (String.valueOf(randomNum).length() < 12) {
            return String.format("1234%012d", randomNum);
        }

        return "1234" + randomNum;
    }

}
