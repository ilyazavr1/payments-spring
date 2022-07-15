package ua.epma.paymentsspring.model.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.epma.paymentsspring.model.dto.PaymentDto;
import ua.epma.paymentsspring.model.entity.Card;
import ua.epma.paymentsspring.model.entity.Payment;
import ua.epma.paymentsspring.model.exception.BlockedCardException;
import ua.epma.paymentsspring.model.exception.InvalidBalanceOnCardException;
import ua.epma.paymentsspring.model.exception.InvalidCardNumberException;
import ua.epma.paymentsspring.model.repository.CardRepository;
import ua.epma.paymentsspring.model.repository.PaymentRepository;
import ua.epma.paymentsspring.model.repository.UserRepository;

import java.time.LocalDateTime;



/**
 * Service for business logic related with Payment.
 *
 * @author Illia
 */
@AllArgsConstructor
@Service
public class PaymentService {

    private PaymentRepository paymentRepository;
    private CardRepository cardRepository;
    private CardService cardService;
    private UserRepository userRepository;


    public Page<Payment> getPaymentPagination(Pageable pageable) {
        return paymentRepository.findPaymentsByUserIdOrUserDestinationId(userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()),
                userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()),
                pageable);
    }

    /**
     * Gets a card from which money is withdrawn from database.
     * Gets a card that replenishes from database.
     * Calls the method that transfers money.
     * Creates new Payment and saves to database.
     *
     * @param paymentDto PaymentDto object that contains validated information for creating Payment.
     * @throws InvalidCardNumberException    if card number does not exist in the database
     * @throws InvalidBalanceOnCardException if the difference between card balance and payment money is negative
     * @throws BlockedCardException          if the card is blocked
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public void makePayment(PaymentDto paymentDto) throws InvalidCardNumberException, InvalidBalanceOnCardException, BlockedCardException {
        Card cardFrom = cardService.getCardByCurrentUserByNumber(paymentDto.getCardSenderNumber());
        Card cardTo = cardRepository.findByNumber(paymentDto.getCardDestinationNumber());

        if (cardService.transferMoney(cardFrom, cardTo, paymentDto.getMoney())) {
            Payment payment = createPaymentWithStatus(paymentDto, cardFrom, cardTo, true);
            payment.setBalance(cardFrom.getMoney());
            payment.setBalanceDestination(cardTo.getMoney());

            paymentRepository.save(payment);

            paymentRepository.updatePreparedPaymentsByCardIds(payment.getCardSenderId().getId(), payment.getCardSenderId().getMoney(),
                    payment.getCardDestinationId().getId(), payment.getCardDestinationId().getMoney());
        }
    }




    /**
     * Gets prepared Payment from database.
     * Calls the method that transfers money.
     * Updates Payment and confirmed it.
     *
     * @param id Payment id
     * @throws InvalidBalanceOnCardException if the difference between card balance and payment money is negative
     * @throws InvalidCardNumberException    if card number does not exist in the database
     * @throws BlockedCardException          if the card is blocked
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public void confirmPayment(Long id) throws InvalidBalanceOnCardException, InvalidCardNumberException, BlockedCardException {
        Payment payment = paymentRepository.getPaymentById(id);

        if (cardService.transferMoney(payment.getCardSenderId(), payment.getCardDestinationId(), payment.getMoney())) {
            payment.setBalance(payment.getCardSenderId().getMoney());
            payment.setBalanceDestination(payment.getCardDestinationId().getMoney());
            payment.setSend(true);
            payment.setCreationTimestamp(LocalDateTime.now());

            paymentRepository.save(payment);

            paymentRepository.updatePreparedPaymentsByCardIds(payment.getCardSenderId().getId(), payment.getCardSenderId().getMoney(),
                    payment.getCardDestinationId().getId(), payment.getCardDestinationId().getMoney());
        }
    }

    /**
     * Gets a card from which money is withdrawn from database.
     * Gets a card that replenishes from database.
     * Validates transfer data.
     * Create prepared Payment and saves it to database.
     *
     * @param paymentDto PaymentDto object that contains validated information for creating Payment.
     * @throws InvalidCardNumberException    if card number does not exist in the database
     * @throws InvalidBalanceOnCardException if the difference between card balance and payment money is negative
     * @throws BlockedCardException          if the card is blocked
     */
    public void createPreparedPayment(PaymentDto paymentDto) throws InvalidCardNumberException, InvalidBalanceOnCardException, BlockedCardException {
        Card cardFrom = cardService.getCardByCurrentUserByNumber(paymentDto.getCardSenderNumber());
        Card cardTo = cardRepository.findByNumber(paymentDto.getCardDestinationNumber());

        cardService.validateTransfer(cardFrom, cardTo, paymentDto.getMoney());

        paymentRepository.save(createPaymentWithStatus(paymentDto, cardFrom, cardTo, false));
    }


    /**
     * @param paymentDto PaymentDto object that contains validated information for creating Payment.
     * @param cardFrom   Card for creating Payment.
     * @param cardTo     Card for creating Payment.
     * @param iSend      if false, creates prepared payment, if true, creates sent payment
     * @return new Payment
     */
    private Payment createPaymentWithStatus(PaymentDto paymentDto, Card cardFrom, Card cardTo, boolean iSend) {
        return Payment.builder()
                .balance(cardFrom.getMoney())
                .balanceDestination(cardTo.getMoney())
                .money(paymentDto.getMoney())
                .isSend(iSend)
                .creationTimestamp(LocalDateTime.now())
                .userId(cardFrom.getUserId())
                .userDestinationId(cardTo.getUserId())
                .cardSenderId(cardFrom)
                .cardDestinationId(cardTo)
                .build();

    }


}
