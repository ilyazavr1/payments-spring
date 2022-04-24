package ua.epma.paymentsspring.model.service;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.context.junit4.SpringRunner;
import ua.epma.paymentsspring.model.entity.Card;
import ua.epma.paymentsspring.model.entity.Role;
import ua.epma.paymentsspring.model.entity.User;

import ua.epma.paymentsspring.model.excwption.BlockedCardException;
import ua.epma.paymentsspring.model.excwption.InvalidCardName;
import ua.epma.paymentsspring.model.excwption.InvalidCardNumberException;
import ua.epma.paymentsspring.model.excwption.InvalidMoneyAmountException;
import ua.epma.paymentsspring.model.repository.CardRepository;
import ua.epma.paymentsspring.model.repository.CardUnblockRequestRepository;
import ua.epma.paymentsspring.model.repository.UserRepository;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class CardServiceTest {

    @Autowired
    CardService cardService;
    @MockBean
    private CardRepository cardRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CardUnblockRequestRepository cardUnblockRequestRepository;

    private User REGISTERED_USER = User.builder().id(1L).firstName(FIRST_NAME).lastName(LAST_NAME).surname(SURNAME)
            .email(REGISTERED_EMAIL).password(HASHED_PASSWORD).role(CLIENT_ROLE).blocked(false).build();

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
    private final static String CARD_NAME = "TEST_NAME";
    private final static String INVALID_CARD_NAME = "TEST_NAMETEST_NAMETEST_NAMETEST_NAMETEST_NAMETEST_NAMETEST_NAMETEST_NAME";
    private final static String CARD_NUMBER = "1234123412341234";
    private final static String MONEY = "100";
    private final static String INVALID_MONEY = "10000000000";

    @BeforeEach
    void setUp() {
        CARD = Card.builder().name(CARD_NAME).number(CARD_NUMBER).userId(REGISTERED_USER).money(Integer.parseInt(MONEY))
                .blocked(false).underConsideration(false).build();
    }


    @Test
    void updateCardWithMoneyThrowsRuntimeException() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);


        when(userRepository.findByEmail(securityContext.getAuthentication().getName())).thenReturn(REGISTERED_USER);
        when(cardRepository.findByNumberAndUserId(CARD_NUMBER, REGISTERED_USER)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> cardService.updateCardWithMoney(CARD_NUMBER, MONEY));
    }


    @Test
    void updateCardWithMoneyThrowsBlockedCardException() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        CARD.setBlocked(true);
        when(userRepository.findByEmail(securityContext.getAuthentication().getName())).thenReturn(REGISTERED_USER);
        when(cardRepository.findByNumberAndUserId(CARD_NUMBER, REGISTERED_USER)).thenReturn(CARD);

        assertThrows(BlockedCardException.class, () -> cardService.updateCardWithMoney(CARD_NUMBER, MONEY));
    }

    @Test
    void updateCardWithMoneyThrowsInvalidMoneyAmountException() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);


        when(userRepository.findByEmail(securityContext.getAuthentication().getName())).thenReturn(REGISTERED_USER);
        when(cardRepository.findByNumberAndUserId(CARD_NUMBER, REGISTERED_USER)).thenReturn(CARD);

        assertThrows(InvalidMoneyAmountException.class, () -> cardService.updateCardWithMoney(CARD_NUMBER, INVALID_MONEY));
    }

    @Test
    void updateCardWithMoneyDoesNotThrowsException() throws BlockedCardException, InvalidMoneyAmountException {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);


        when(userRepository.findByEmail(securityContext.getAuthentication().getName())).thenReturn(REGISTERED_USER);
        when(cardRepository.findByNumberAndUserId(CARD_NUMBER, REGISTERED_USER)).thenReturn(CARD);

        cardService.updateCardWithMoney(CARD_NUMBER, MONEY);

        verify(cardRepository, times(1)).save(CARD);

    }


    @Test
    void createCardThrowsInvalidCardNameException() {
        assertThrows(InvalidCardName.class, () -> cardService.createCard(INVALID_CARD_NAME));
    }


    @Test
    void createCardShouldNotSave() throws InvalidCardName {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(securityContext.getAuthentication().getName())).thenReturn(REGISTERED_USER);
        when(cardRepository.findByNumber(CARD_NUMBER)).thenReturn(CARD);
        cardService.createCard(CARD_NAME);

        verify(cardRepository, times(0)).save(CARD);
    }

    @Test
    void createCardShouldSave() throws InvalidCardName {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);


        when(userRepository.findByEmail(securityContext.getAuthentication().getName())).thenReturn(REGISTERED_USER);
        when(cardRepository.findByNumber(CARD_NUMBER)).thenReturn(null);
        when(cardRepository.save(CARD)).thenReturn(CARD);


        cardService.createCard(CARD_NAME);

        assertNotNull(cardRepository.save(CARD));


    }

}