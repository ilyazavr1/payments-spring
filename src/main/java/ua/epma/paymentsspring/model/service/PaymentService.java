package ua.epma.paymentsspring.model.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ua.epma.paymentsspring.model.dto.PaymentDto;
import ua.epma.paymentsspring.model.entity.Card;
import ua.epma.paymentsspring.model.entity.Payment;
import ua.epma.paymentsspring.model.excwption.BlockedCardException;
import ua.epma.paymentsspring.model.excwption.InvalidBalanceOnCardException;
import ua.epma.paymentsspring.model.excwption.InvalidCardNumberException;
import ua.epma.paymentsspring.model.repository.CardRepository;
import ua.epma.paymentsspring.model.repository.PaymentRepository;
import ua.epma.paymentsspring.model.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@Service
public class PaymentService {

    private PaymentRepository paymentRepository;
    private CardRepository cardRepository;
    private CardService cardService;
    private UserRepository userRepository;


    public Payment getPaymentById(Long id) {
        return paymentRepository.getPaymentById(id);
    }


    public Page<Payment> getPaymentPagination(Pageable pageable){


        return paymentRepository.findPaymentsByUserId(userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()),pageable);
    }



    public void makePayment(PaymentDto paymentDto) throws InvalidCardNumberException, InvalidBalanceOnCardException, BlockedCardException {
        Card cardFrom = cardService.getCardByCurrentUserByNumber(paymentDto.getCardSenderNumber());
        Card cardTo = cardRepository.findByNumber(paymentDto.getCardDestinationNumber());

        if (cardService.transferMoney(cardFrom, cardTo, paymentDto.getMoney())) {
            Payment payment = createPaymentWithStatus(paymentDto, cardFrom, cardTo, true);
            payment.setBalance(cardFrom.getMoney() + payment.getMoney());
            paymentRepository.save(payment);
        }
    }

    public void confirmPayment(Long id) throws InvalidBalanceOnCardException, InvalidCardNumberException, BlockedCardException {
        Payment payment = paymentRepository.getPaymentById(id);
        if (cardService.transferMoney(payment.getCardSenderId(), payment.getCardDestinationId(), payment.getMoney())) {
            payment.setBalance(payment.getBalance() - payment.getMoney());
            payment.setSend(true);
            payment.setCreationTimestamp(LocalDateTime.now());
            paymentRepository.save(payment);
        }
    }

    public void createPreparedPayment(PaymentDto paymentDto) throws InvalidCardNumberException, InvalidBalanceOnCardException, BlockedCardException {
        Card cardFrom = cardService.getCardByCurrentUserByNumber(paymentDto.getCardSenderNumber());
        Card cardTo = cardRepository.findByNumber(paymentDto.getCardDestinationNumber());


        cardService.validateTransfer(cardFrom, cardTo, paymentDto.getMoney());


        paymentRepository.save(createPaymentWithStatus(paymentDto, cardFrom, cardTo, false));
    }


    private Payment createPaymentWithStatus(PaymentDto paymentDto, Card cardFrom, Card cardTo, boolean iSend) {
        return Payment.builder()
                .balance(cardFrom.getMoney())
                .money(paymentDto.getMoney())
                .isSend(iSend)
                .creationTimestamp(LocalDateTime.now())
                .userId(cardFrom.getUserId())
                .cardSenderId(cardFrom)
                .cardDestinationId(cardTo)
                .build();

    }

    public void updatePreparedPayments(){
        paymentRepository.getPaymentByUserId(userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    public List<Payment> getPaymentsByCurrentUser() {
        return paymentRepository.getPaymentByUserId(userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
    }
}
