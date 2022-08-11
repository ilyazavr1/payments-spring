package ua.epma.paymentsspring.model.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.epma.paymentsspring.model.dto.PaymentDto;
import ua.epma.paymentsspring.model.entity.Card;
import ua.epma.paymentsspring.model.entity.Payment;
import ua.epma.paymentsspring.model.entity.Role;
import ua.epma.paymentsspring.model.entity.User;
import ua.epma.paymentsspring.exception.BlockedCardException;
import ua.epma.paymentsspring.exception.InvalidBalanceOnCardException;
import ua.epma.paymentsspring.exception.InvalidCardNumberException;
import ua.epma.paymentsspring.model.repository.CardRepository;
import ua.epma.paymentsspring.model.repository.PaymentRepository;
import ua.epma.paymentsspring.model.repository.UserRepository;
import ua.epma.paymentsspring.service.CardService;
import ua.epma.paymentsspring.service.PaymentService;

import java.time.LocalDateTime;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private CardService cardService;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    PaymentService paymentService;


    private Payment PAYMENT;
    private PaymentDto PAYMENT_DTO;
    private Card CARD1;
    private Card CARD2;
    private User REGISTERED_USER;
    private final static String CARD_NAME = "TEST_NAME";
    private final static String INVALID_CARD_NAME = "TEST_NAMETEST_NAMETEST_NAMETEST_NAMETEST_NAMETEST_NAMETEST_NAMETEST_NAME";
    private final static String CARD_NUMBER_1 = "1234123412341234";
    private final static String CARD_NUMBER_2 = "6345223412341234";
    private final static String MONEY100 = "100";
    private final static int MONEY_INT100 = 100;
    private final static String INVALID_MONEY = "10000000000";
    private final static Role CLIENT_ROLE = Role.builder().id(1L).roleEnum(Role.RoleEnum.CLIENT).build();
    private final static String FIRST_NAME = "TEST_NAME";
    private final static String LAST_NAME = "TEST_LAST_NAME";
    private final static String SURNAME = "TEST_SURNAME";
    private final static String REGISTERED_EMAIL = "test@gmail.con";
    private final static String PASSWORD = "Qwerty12345";
    private final static String NOT_REGISTERED_EMAIL = "qweqwqweqwe@gmail.con";
    private final static String RANDOM_PASSWORD = "Qwerty12fas345";
    private final static String HASHED_PASSWORD = "$2a$10$GJwpUE5I3ZSYaPsgpamkN.CO8GDjFDB7HjDVBC6vXyo0kmwkIqqUS";

    @BeforeEach
    void setUp() {
        PAYMENT_DTO = PaymentDto.builder().cardSenderNumber(CARD_NUMBER_1).cardDestinationNumber(CARD_NUMBER_2)
                .money(MONEY_INT100).build();
        REGISTERED_USER = User.builder().id(1L).firstName(FIRST_NAME).lastName(LAST_NAME).patronymic(SURNAME)
                .email(REGISTERED_EMAIL).password(HASHED_PASSWORD).role(CLIENT_ROLE).blocked(false).build();

        CARD1 = Card.builder().id(1L).name(CARD_NAME).number(CARD_NUMBER_1).userId(REGISTERED_USER).money(MONEY_INT100)
                .blocked(false).underConsideration(false).build();
        CARD2 = Card.builder().name(CARD_NAME).number(CARD_NUMBER_2).userId(REGISTERED_USER).money(MONEY_INT100)
                .blocked(false).underConsideration(false).build();

        PAYMENT = Payment.builder().id(1L).balance(CARD1.getMoney()).balance(CARD2.getMoney())
                .money(PAYMENT_DTO.getMoney()).isSend(false).creationTimestamp(LocalDateTime.now())
                .userId(CARD1.getUserId()).userDestinationId(CARD2.getUserId())
                .cardSenderId(CARD1).cardDestinationId(CARD2).build();
    }


    @Test
    void makePaymentThrowsInvalidCardNumberException() throws InvalidBalanceOnCardException, InvalidCardNumberException, BlockedCardException {
        when(cardRepository.findByNumber(CARD_NUMBER_1)).thenReturn(CARD1);
        when(cardRepository.findByNumber(CARD_NUMBER_2)).thenReturn(CARD2);
        when(cardService.transferMoney(CARD1, CARD2, MONEY_INT100)).thenThrow(InvalidCardNumberException.class);

        assertThrows(InvalidCardNumberException.class, () -> paymentService.makePayment(PAYMENT_DTO));
    }

    @Test
    void makePaymentThrowsInvalidBalanceOnCardException() throws InvalidBalanceOnCardException, InvalidCardNumberException, BlockedCardException {
        when(cardRepository.findByNumber(CARD_NUMBER_1)).thenReturn(CARD1);
        when(cardRepository.findByNumber(CARD_NUMBER_2)).thenReturn(CARD2);
        when(cardService.transferMoney(CARD1, CARD2, MONEY_INT100)).thenThrow(InvalidBalanceOnCardException.class);

        assertThrows(InvalidBalanceOnCardException.class, () -> paymentService.makePayment(PAYMENT_DTO));
    }

    @Test
    void makePaymentThrowsBlockedCardException() throws InvalidBalanceOnCardException, InvalidCardNumberException, BlockedCardException {
        when(cardRepository.findByNumber(CARD_NUMBER_1)).thenReturn(CARD1);
        when(cardRepository.findByNumber(CARD_NUMBER_2)).thenReturn(CARD2);
        when(cardService.transferMoney(CARD1, CARD2, MONEY_INT100)).thenThrow(BlockedCardException.class);

        assertThrows(BlockedCardException.class, () -> paymentService.makePayment(PAYMENT_DTO));
    }

    @Test
    void makePaymentPaymentDoesNotCreated() throws InvalidBalanceOnCardException, InvalidCardNumberException, BlockedCardException {
        when(cardRepository.findByNumber(CARD_NUMBER_1)).thenReturn(CARD1);
        when(cardRepository.findByNumber(CARD_NUMBER_2)).thenReturn(CARD2);
        when(cardService.transferMoney(CARD1, CARD2, MONEY_INT100)).thenReturn(false);

        paymentService.makePayment(PAYMENT_DTO);

        verify(paymentRepository, times(0)).save(any());
        verify(paymentRepository, times(0)).updatePreparedPaymentsByCardIds(CARD1.getId(), CARD1.getMoney(), CARD2.getId(), CARD2.getMoney());
    }

    @Test
    void makePayment() throws InvalidBalanceOnCardException, InvalidCardNumberException, BlockedCardException {
        when(cardRepository.findByNumber(CARD_NUMBER_1)).thenReturn(CARD1);
        when(cardRepository.findByNumber(CARD_NUMBER_2)).thenReturn(CARD2);
        when(cardService.transferMoney(CARD1, CARD2, MONEY_INT100)).thenReturn(true);

        paymentService.makePayment(PAYMENT_DTO);

        verify(paymentRepository, times(1)).save(any());
        verify(paymentRepository, times(1)).updatePreparedPaymentsByCardIds(CARD1.getId(), CARD1.getMoney(), CARD2.getId(), CARD2.getMoney());
    }

    @Test
    void confirmPaymentThrowsInvalidCardNumberException() throws InvalidBalanceOnCardException, InvalidCardNumberException, BlockedCardException {
        when(paymentRepository.getPaymentById(PAYMENT.getId())).thenReturn(PAYMENT);
        when(cardService.transferMoney(PAYMENT.getCardSenderId(), PAYMENT.getCardDestinationId(), PAYMENT.getMoney()))
                .thenThrow(InvalidCardNumberException.class);

        assertThrows(InvalidCardNumberException.class, () -> paymentService.confirmPayment(PAYMENT.getId()));
    }


    @Test
    void confirmPaymentThrowsInvalidBalanceOnCardException() throws InvalidBalanceOnCardException, InvalidCardNumberException, BlockedCardException {
        when(paymentRepository.getPaymentById(PAYMENT.getId())).thenReturn(PAYMENT);
        when(cardService.transferMoney(PAYMENT.getCardSenderId(), PAYMENT.getCardDestinationId(), PAYMENT.getMoney()))
                .thenThrow(InvalidBalanceOnCardException.class);

        assertThrows(InvalidBalanceOnCardException.class, () -> paymentService.confirmPayment(PAYMENT.getId()));
    }


    @Test
    void confirmPaymentThrowsBlockedCardException() throws InvalidBalanceOnCardException, InvalidCardNumberException, BlockedCardException {
        when(paymentRepository.getPaymentById(PAYMENT.getId())).thenReturn(PAYMENT);
        when(cardService.transferMoney(PAYMENT.getCardSenderId(), PAYMENT.getCardDestinationId(), PAYMENT.getMoney()))
                .thenThrow(BlockedCardException.class);

        assertThrows(BlockedCardException.class, () -> paymentService.confirmPayment(PAYMENT.getId()));
    }


    @Test
    void confirmPaymentDoesNotConfirmsPayment() throws InvalidBalanceOnCardException, InvalidCardNumberException, BlockedCardException {
        when(paymentRepository.getPaymentById(PAYMENT.getId())).thenReturn(PAYMENT);
        when(cardService.transferMoney(PAYMENT.getCardSenderId(), PAYMENT.getCardDestinationId(), PAYMENT.getMoney()))
                .thenReturn(false);

        paymentService.confirmPayment(PAYMENT.getId());

        verify(paymentRepository, times(0)).save(PAYMENT);
        verify(paymentRepository, times(0))
                .updatePreparedPaymentsByCardIds(
                        PAYMENT.getCardSenderId().getId(),
                        PAYMENT.getCardSenderId().getMoney(),
                        PAYMENT.getCardDestinationId().getId(),
                        PAYMENT.getCardDestinationId().getMoney()
                );
    }


    @Test
    void confirmPayment() throws InvalidBalanceOnCardException, InvalidCardNumberException, BlockedCardException {
        when(paymentRepository.getPaymentById(PAYMENT.getId())).thenReturn(PAYMENT);
        when(cardService.transferMoney(PAYMENT.getCardSenderId(), PAYMENT.getCardDestinationId(), PAYMENT.getMoney()))
                .thenReturn(true);

        paymentService.confirmPayment(PAYMENT.getId());

        verify(paymentRepository, times(1)).save(PAYMENT);
        verify(paymentRepository, times(1))
                .updatePreparedPaymentsByCardIds(
                        PAYMENT.getCardSenderId().getId(),
                        PAYMENT.getCardSenderId().getMoney(),
                        PAYMENT.getCardDestinationId().getId(),
                        PAYMENT.getCardDestinationId().getMoney()
                );
    }

    @Test
    void createPreparedPaymentThrowsInvalidBalanceOnCardException() throws InvalidBalanceOnCardException, InvalidCardNumberException, BlockedCardException {
        when(cardRepository.findByNumber(CARD_NUMBER_1)).thenReturn(CARD1);
        when(cardRepository.findByNumber(CARD_NUMBER_2)).thenReturn(CARD2);
        when(cardService.validateTransfer(CARD1, CARD2, MONEY_INT100)).thenThrow(InvalidBalanceOnCardException.class);

        assertThrows(InvalidBalanceOnCardException.class, () -> paymentService.createPreparedPayment(PAYMENT_DTO));
    }

    @Test
    void createPreparedPaymentThrowsInvalidCardNumberException() throws InvalidBalanceOnCardException, InvalidCardNumberException, BlockedCardException {
        when(cardRepository.findByNumber(CARD_NUMBER_1)).thenReturn(CARD1);
        when(cardRepository.findByNumber(CARD_NUMBER_2)).thenReturn(CARD2);
        when(cardService.validateTransfer(CARD1, CARD2, MONEY_INT100)).thenThrow(InvalidCardNumberException.class);

        assertThrows(InvalidCardNumberException.class, () -> paymentService.createPreparedPayment(PAYMENT_DTO));
    }

    @Test
    void createPreparedPaymentThrowsBlockedCardException() throws InvalidBalanceOnCardException, InvalidCardNumberException, BlockedCardException {
        when(cardRepository.findByNumber(CARD_NUMBER_1)).thenReturn(CARD1);
        when(cardRepository.findByNumber(CARD_NUMBER_2)).thenReturn(CARD2);
        when(cardService.validateTransfer(CARD1, CARD2, MONEY_INT100)).thenThrow(BlockedCardException.class);

        assertThrows(BlockedCardException.class, () -> paymentService.createPreparedPayment(PAYMENT_DTO));
    }

    @Test
    void createPreparedPaymentDoesNotCreatesPreparedPayment() throws InvalidBalanceOnCardException, InvalidCardNumberException, BlockedCardException {
        when(cardRepository.findByNumber(CARD_NUMBER_1)).thenReturn(CARD1);
        when(cardRepository.findByNumber(CARD_NUMBER_2)).thenReturn(CARD2);
        when(cardService.validateTransfer(CARD1, CARD2, PAYMENT_DTO.getMoney())).thenReturn(false);

        paymentService.createPreparedPayment(PAYMENT_DTO);

        verify(paymentRepository, times(0)).save(any(Payment.class));
    }

    @Test
    void createPreparedPayment() throws InvalidBalanceOnCardException, InvalidCardNumberException, BlockedCardException {
        when(cardRepository.findByNumber(CARD_NUMBER_1)).thenReturn(CARD1);
        when(cardRepository.findByNumber(CARD_NUMBER_2)).thenReturn(CARD2);
        when(cardService.validateTransfer(CARD1, CARD2, PAYMENT_DTO.getMoney())).thenReturn(true);

        paymentService.createPreparedPayment(PAYMENT_DTO);

        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

}