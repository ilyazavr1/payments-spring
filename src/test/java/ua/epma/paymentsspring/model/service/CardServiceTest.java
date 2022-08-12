package ua.epma.paymentsspring.model.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ua.epma.paymentsspring.exception.*;
import ua.epma.paymentsspring.model.entity.Card;
import ua.epma.paymentsspring.model.entity.CardUnblockRequest;
import ua.epma.paymentsspring.model.entity.Role;
import ua.epma.paymentsspring.model.entity.User;
import ua.epma.paymentsspring.model.repository.CardRepository;
import ua.epma.paymentsspring.model.repository.CardUnblockRequestRepository;
import ua.epma.paymentsspring.model.repository.PaymentRepository;
import ua.epma.paymentsspring.model.repository.UserRepository;
import ua.epma.paymentsspring.service.CardService;

import java.util.Collection;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CardServiceTest {


    @Mock
    private CardRepository cardRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CardUnblockRequestRepository cardUnblockRequestRepository;
    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    CardService cardService;

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
    private final static int MONEY_INT100 = 100;
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
    void updateCardWithMoneyThrowsRuntimeException() throws BlockedCardException, InvalidMoneyAmountException {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);


        when(userRepository.findByEmail(securityContext.getAuthentication().getName())).thenReturn(REGISTERED_USER);
        when(cardRepository.findByNumberAndUserId(CARD_NUMBER_1, REGISTERED_USER)).thenReturn(null);
        cardService.updateCardWithMoney(CARD_NUMBER_1, MONEY100);

        verify(cardRepository, times(0)).save(null);
    }

    @Test
    void updateCardWithMoneyThrowsBlockedCardException() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        CARD.setBlocked(true);
        when(userRepository.findByEmail(securityContext.getAuthentication().getName())).thenReturn(REGISTERED_USER);
        when(cardRepository.findByNumberAndUserId(CARD_NUMBER_1, REGISTERED_USER)).thenReturn(CARD);

        assertThrows(BlockedCardException.class, () -> cardService.updateCardWithMoney(CARD_NUMBER_1, MONEY100));
    }

    @Test
    void updateCardWithMoneyThrowsInvalidMoneyAmountException() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);


        when(userRepository.findByEmail(securityContext.getAuthentication().getName())).thenReturn(REGISTERED_USER);
        when(cardRepository.findByNumberAndUserId(CARD_NUMBER_1, REGISTERED_USER)).thenReturn(CARD);

        assertThrows(InvalidMoneyAmountException.class, () -> cardService.updateCardWithMoney(CARD_NUMBER_1, INVALID_MONEY));
    }

    @Test
    void updateCardWithMoneyDoesNotThrowsException() throws BlockedCardException, InvalidMoneyAmountException {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);


        when(userRepository.findByEmail(securityContext.getAuthentication().getName())).thenReturn(REGISTERED_USER);
        when(cardRepository.findByNumberAndUserId(CARD_NUMBER_1, REGISTERED_USER)).thenReturn(CARD);

        cardService.updateCardWithMoney(CARD_NUMBER_1, MONEY100);

        verify(cardRepository, times(1)).save(CARD);

    }

    @Test
    void createCardThrowsInvalidCardNameException() {
        assertThrows(InvalidCardName.class, () -> cardService.createCardAndAddToUser(INVALID_CARD_NAME, ""));
    }

    @Test
    void createCardShouldNotSave() throws InvalidCardName {
     /*   Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(securityContext.getAuthentication().getName())).thenReturn(REGISTERED_USER);*/
        when(cardRepository.findByNumber(anyString())).thenReturn(CARD);

        cardService.createCardAndAddToUser(CARD_NAME, "");

        verify(cardRepository, times(0)).save(CARD);
    }

    @Test
    void createCardShouldSave() throws InvalidCardName {



        when(userRepository.findByEmail(REGISTERED_EMAIL)).thenReturn(REGISTERED_USER);
        // when(userRepository.findByEmail(REGISTERED_EMAIL)).thenReturn(REGISTERED_USER);

        when(cardRepository.findByNumber(anyString())).thenReturn(null);

        when(cardRepository.save(any(Card.class))).thenReturn(any(Card.class));


        cardService.createCardAndAddToUser(CARD_NAME, REGISTERED_EMAIL);


        verify(cardRepository, times(1)).save(any(Card.class));


    }

    @Test
    void unblockCardByNumberThrowsInvalidCardNumberException() {
        when(cardRepository.findByNumber(CARD_NUMBER_1)).thenReturn(null);

        assertThrows(InvalidCardNumberException.class, () -> cardService.unblockCardByNumber(CARD_NUMBER_1));
    }

    @Test
    void unblockCardByNumberUnblockCardIfUnderConsiderationIsFalse() throws InvalidCardNumberException {
        CARD.setBlocked(true);

        when(cardRepository.findByNumber(CARD_NUMBER_1)).thenReturn(CARD);
        when(cardRepository.save(CARD)).thenReturn(CARD);

        cardService.unblockCardByNumber(CARD_NUMBER_1);

        verify(cardUnblockRequestRepository, times(0)).findTopByCardIdOrderByIdDesc(CARD);
        verify(cardRepository, times(1)).save(CARD);
    }

    @Test
    void unblockCardByNumberUnblockCardIfUnderConsiderationIsTrue() throws InvalidCardNumberException {
        CARD.setBlocked(true);
        CARD.setUnderConsideration(true);
        CARD_REQUEST_PROCESSED_COPY.setProcessed(true);

        when(cardRepository.findByNumber(CARD_NUMBER_1)).thenReturn(CARD);
        when(cardUnblockRequestRepository.findTopByCardIdOrderByIdDesc(CARD)).thenReturn(CARD_REQUEST_PROCESSED_FALSE);
        when(cardUnblockRequestRepository.save(CARD_REQUEST_PROCESSED_COPY)).thenReturn(CARD_REQUEST_PROCESSED_COPY);
        when(cardRepository.save(CARD)).thenReturn(CARD_COPY);

        cardService.unblockCardByNumber(CARD_NUMBER_1);

        verify(cardUnblockRequestRepository, times(1)).findTopByCardIdOrderByIdDesc(CARD);
        verify(cardUnblockRequestRepository, times(1)).save(CARD_REQUEST_PROCESSED_COPY);
        verify(cardRepository, times(1)).save(CARD);
    }

    @Test
    void blockCardByNumberAdministratorDoNotBlockCard() throws InvalidCardNumberException, AuthenticationFailedException {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Collection cl = mock(Collection.class);
        when(authentication.getAuthorities()).thenReturn(cl);
        when(cl.toString()).thenReturn("[ADMINISTRATOR]");


        when(cardRepository.findByNumber(CARD_NUMBER_1)).thenReturn(null);


        cardService.blockCardByNumber(CARD_NUMBER_1, PASSWORD);

        verify(cardRepository, times(0)).save(CARD);
    }

    @Test
    void blockCardByNumberAdministratorThrowAuthenticationFailedException() throws InvalidCardNumberException, AuthenticationFailedException {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Collection cl = mock(Collection.class);
        when(authentication.getAuthorities()).thenReturn(cl);
        when(cl.toString()).thenReturn("[ADMINISTRATOR]");


        when(userRepository.findByEmail(securityContext.getAuthentication().getName())).thenReturn(REGISTERED_USER);
        when(cardRepository.findByNumber(CARD_NUMBER_1)).thenReturn(CARD);


        assertThrows(AuthenticationFailedException.class, () -> cardService.blockCardByNumber(CARD_NUMBER_1, RANDOM_PASSWORD));

        verify(cardRepository, times(0)).save(CARD);
    }

    @Test
    void blockCardByNumberAdministratorBlocksCard() throws InvalidCardNumberException, AuthenticationFailedException {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Collection cl = mock(Collection.class);
        when(authentication.getAuthorities()).thenReturn(cl);
        when(cl.toString()).thenReturn("[ADMINISTRATOR]");


        when(userRepository.findByEmail(securityContext.getAuthentication().getName())).thenReturn(REGISTERED_USER);
        when(cardRepository.findByNumber(CARD_NUMBER_1)).thenReturn(CARD);

        cardService.blockCardByNumber(CARD_NUMBER_1, PASSWORD);

        CARD.setBlocked(true);
        verify(cardRepository, times(1)).save(CARD);
    }

    @Test
    void blockCardByNumberClientThrowsInvalidCardNumberException() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Collection cl = mock(Collection.class);

        when(authentication.getAuthorities()).thenReturn(cl);
        when(cl.toString()).thenReturn("[CLIENT]");

        when(userRepository.findByEmail(securityContext.getAuthentication().getName())).thenReturn(REGISTERED_USER);
        when(cardRepository.findByNumberAndUserId(CARD_NUMBER_1, userRepository.findByEmail(securityContext.getAuthentication().getName()))).thenReturn(null);

        assertThrows(InvalidCardNumberException.class, () -> cardService.blockCardByNumber(CARD_NUMBER_1, PASSWORD));

    }


    @Test
    void transferMoneyThrowInvalidCardNumberException() {
        CARD_COPY.setNumber(CARD.getNumber());

        assertThrows(InvalidCardNumberException.class, () -> cardService.transferMoney(CARD, null, MONEY_INT100));
        assertThrows(InvalidCardNumberException.class, () -> cardService.transferMoney(CARD, CARD_COPY, MONEY_INT100));
    }

    @Test
    void transferMoneyThrowBlockedCardException() {
        CARD_COPY.setBlocked(true);
        CARD.setBlocked(true);

        assertThrows(InvalidCardNumberException.class, () -> cardService.transferMoney(CARD, CARD_COPY, MONEY_INT100));
    }

    @Test
    void transferMoneyThrowInvalidBalanceOnCardException() {
        CARD.setNumber(CARD_NUMBER_1);
        CARD.setMoney(MONEY_INT100 - 1);
        CARD_COPY.setNumber(CARD_NUMBER_2);

        assertThrows(InvalidBalanceOnCardException.class, () -> cardService.transferMoney(CARD, CARD_COPY, MONEY_INT100));
    }

    @Test
    void transferMoney() throws InvalidBalanceOnCardException, InvalidCardNumberException, BlockedCardException {
        CARD.setNumber(CARD_NUMBER_1);
        CARD_COPY.setNumber(CARD_NUMBER_2);

        cardService.transferMoney(CARD, CARD_COPY, MONEY_INT100);

        verify(cardRepository, times(1)).save(CARD);
        verify(cardRepository, times(1)).save(CARD_COPY);
    }

    @Test
    void processedCardUnblockRequest() {
        when(cardUnblockRequestRepository.getById(CARD_REQUEST_PROCESSED_FALSE.getId())).thenReturn(CARD_REQUEST_PROCESSED_FALSE);

        cardService.processedCardUnblockRequest(CARD_REQUEST_PROCESSED_FALSE.getId());

        verify(cardUnblockRequestRepository, times(1)).save(CARD_REQUEST_PROCESSED_FALSE);
    }

    @Test
    void makeCardUnblockRequestThrowInvalidCardNumberException() {
        when(cardRepository.findByNumber(CARD_NUMBER_1)).thenReturn(null);

        assertThrows(InvalidCardNumberException.class, () -> cardService.makeCardUnblockRequest(CARD_NUMBER_1));
    }

    @Test
    void makeCardUnblockRequest() throws InvalidCardNumberException {
        when(cardRepository.findByNumber(CARD_NUMBER_1)).thenReturn(CARD);


        cardService.makeCardUnblockRequest(CARD_NUMBER_1);

        verify(cardRepository, times(1)).save(CARD);

        verify(cardUnblockRequestRepository, times(1)).save(CARD_REQUEST_PROCESSED_FALSE);
    }

}