import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ua.epma.paymentsspring.exception.BlockedCardException;
import ua.epma.paymentsspring.exception.InvalidBalanceOnCardException;
import ua.epma.paymentsspring.exception.InvalidCardNumberException;
import ua.epma.paymentsspring.model.entity.Card;
import ua.epma.paymentsspring.model.entity.CardUnblockRequest;
import ua.epma.paymentsspring.model.entity.Role;
import ua.epma.paymentsspring.model.entity.User;
import ua.epma.paymentsspring.service.CardService;

import java.math.BigDecimal;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
@RunWith(SpringJUnit4ClassRunner.class)
public class TestingTest {

    @Autowired
   private CardService cardService;
    private User REGISTERED_USER;
    private User COPY_USER;
    private final static Role CLIENT_ROLE = Role.builder().id(1L).roleEnum(Role.RoleEnum.CLIENT).build();
    private final static String FIRST_NAME = "TEST_NAME";
    private final static String LAST_NAME = "TEST_LAST_NAME";
    private final static String SURNAME = "TEST_SURNAME";
    private final static String REGISTERED_EMAIL = "test@gmail.con";
    private final static String PASSWORD = "Qwerty12345";
    private final static String NOT_REGISTERED_EMAIL = "qweqwqweqwe@gmail.con";
    private final static String RANDOM_PASSWORD = "Qwerty12fas345";
    private final static String HASHED_PASSWORD = "$2a$10$GJwpUE5I3ZSYaPsgpamkN.CO8GDjFDB7HjDVBC6vXyo0kmwkIqqUS";

    private Card CARD;
    private Card CARD_COPY;
    private CardUnblockRequest CARD_REQUEST_PROCESSED_FALSE;
    private CardUnblockRequest CARD_REQUEST_PROCESSED_COPY;
    private final static String CARD_NAME = "TEST_NAME";
    private final static String INVALID_CARD_NAME = "TEST_NAMETEST_NAMETEST_NAMETEST_NAMETEST_NAMETEST_NAMETEST_NAMETEST_NAME";
    private final static String CARD_NUMBER_1 = "1234123412341234";
    private final static String CARD_NUMBER_2 = "6345223412341234";
    private final static String MONEY100 = "100";
    private final static BigDecimal MONEY_INT100 = new BigDecimal(100);
    private final static String INVALID_MONEY = "10000000000";

    @BeforeEach
    void setUp() {
        CARD = Card.builder().id(1L).name(CARD_NAME).number(CARD_NUMBER_1).userId(REGISTERED_USER).money(MONEY_INT100)
                .blocked(false).underConsideration(false).build();
        CARD_COPY = Card.builder().name(CARD_NAME).number(CARD_NUMBER_1).userId(REGISTERED_USER).money(MONEY_INT100)
                .blocked(false).underConsideration(false).build();

        CARD_REQUEST_PROCESSED_FALSE = CardUnblockRequest.builder().cardId(CARD).userId(REGISTERED_USER).isProcessed(false).build();
        CARD_REQUEST_PROCESSED_COPY = CardUnblockRequest.builder().cardId(CARD).userId(REGISTERED_USER).isProcessed(false).build();

        REGISTERED_USER = User.builder().id(1L).firstName(FIRST_NAME).lastName(LAST_NAME).patronymic(SURNAME)
                .email(REGISTERED_EMAIL).password(HASHED_PASSWORD).role(CLIENT_ROLE).blocked(false).build();

        COPY_USER = User.builder().id(1L).firstName(FIRST_NAME).lastName(LAST_NAME).patronymic(SURNAME)
                .email(REGISTERED_EMAIL).password(HASHED_PASSWORD).role(CLIENT_ROLE).blocked(false).build();
    }

    @Test
    @Rollback
    void transferMoney() throws InvalidBalanceOnCardException, InvalidCardNumberException, BlockedCardException {
        CARD.setNumber(CARD_NUMBER_1);
        CARD_COPY.setNumber(CARD_NUMBER_2);

        cardService.transferMoney(CARD, CARD_COPY, MONEY_INT100);

    }
}
