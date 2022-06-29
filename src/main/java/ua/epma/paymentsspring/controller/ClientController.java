package ua.epma.paymentsspring.controller;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.epma.paymentsspring.model.dto.PaymentDto;
import ua.epma.paymentsspring.model.entity.Card;
import ua.epma.paymentsspring.model.entity.Payment;
import ua.epma.paymentsspring.model.entity.User;
import ua.epma.paymentsspring.model.excwption.*;
import ua.epma.paymentsspring.model.service.CardService;
import ua.epma.paymentsspring.model.service.PaymentService;
import ua.epma.paymentsspring.model.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@AllArgsConstructor
@Controller
@RequestMapping("/client")
public class ClientController {

    private final UserService userService;
    private final CardService cardService;
    private final PaymentService paymentService;

    @GetMapping(value = "/cards")
    public String getCards(@RequestParam(defaultValue = "0") String page,
                           @RequestParam(defaultValue = "name") String sort,
                           @RequestParam(defaultValue = "desc") String order,
                           @RequestParam(defaultValue = "9") String size,
                           Model model) {


        Pageable pageable;
        if (order.equals("asc")) {
            pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size), Sort.by(sort).ascending());
        } else pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size), Sort.by(sort).descending());


        Page<Card> cardPage = cardService.getCardPagination(pageable);
        List<Card> list = cardPage.getContent();


        model.addAttribute("size", size);
        model.addAttribute("page", cardPage.getTotalPages());
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
        model.addAttribute("cardList", list);


        return "/client/cards";
    }


    @GetMapping("/payments")
    public String getAllPayments(@RequestParam(defaultValue = "0") String page,
                                 @RequestParam(defaultValue = "id") String sort,
                                 @RequestParam(defaultValue = "desc") String order,
                                 @RequestParam(defaultValue = "9") String size,
                                 Model model)  {


        Pageable pageable;
        if (order.equals("asc")) {
            pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size), Sort.by(sort).ascending());
        } else pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size), Sort.by(sort).descending());


        Page<Payment> paymentPage = paymentService.getPaymentPagination(pageable);
        List<Payment> paymentList = paymentPage.getContent();



        model.addAttribute("size", size);
        model.addAttribute("page", paymentPage.getTotalPages());
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
        model.addAttribute("paymentList", paymentList);

        return "/client/payments";
    }


    @GetMapping("/card/create")
    public String getCardCreate() {

        return "/client/createCard";
    }


    @PostMapping("/card/create")
    public String postCardCreate(@RequestParam(name = "name") String name, ModelMap model) {
        try {
            cardService.createCard(name);
        } catch (InvalidCardName e) {
            log.warn("Invalid card name input: {}", name);
            return "redirect:/client/card/create?error";
        }
        log.info("CLIENT {} created card", SecurityContextHolder.getContext().getAuthentication().getName());
        return "redirect:/client/cards";
    }


    @GetMapping("/card/{number}/top-up")
    public String getCardTopUp(@PathVariable String number, Model model) {
        if (getCard(number, model)) return "redirect:/client/cards";

        return "/client/cardTopUp";
    }

    @PostMapping("/card/{number}/top-up")
    public String postCardTopUp(@RequestParam("money") String money, ModelMap model, @PathVariable String number) {
        try {
            cardService.updateCardWithMoney(number, money);
        } catch (BlockedCardException e) {
            log.warn("CLIENT {} tried replenished BLOCKED card [number: {}] [money: {}]", SecurityContextHolder.getContext().getAuthentication().getName(), number, money.trim());
            return "redirect:/client/cards";
        } catch (InvalidMoneyAmountException e) {
            log.warn("CLIENT {} tried replenished card with wrong money amount [number: {}] [money: {}]", SecurityContextHolder.getContext().getAuthentication().getName(), number, money.trim());
            return "redirect:/client/card/" + number + "/top-up?error";
        }
        log.info("CLIENT {} replenished card [number: {}] [money: {}]", SecurityContextHolder.getContext().getAuthentication().getName(), number, money.trim());
        return "redirect:/client/cards";
    }


    @GetMapping("/card/{number}/block")
    public String getCardBlock(@PathVariable String number, Model model) {
        if (getCard(number, model)) return "redirect:/client/cards";

        return "/client/blockCard";
    }

    private boolean getCard(@PathVariable String number, Model model) {
        if (number == null || number.isEmpty()) return true;
        Card card;
        try {
            card = cardService.getCardByCurrentUserByNumber(number);
            if (card.isBlocked()) {
                log.warn("Card is blocked [number: {}] ", number);
                return true;
            }
            model.addAttribute("card", card);
        } catch (InvalidCardNumberException e) {
            log.warn("Wrong card number [number: {}] ", number);
            return true;
        }
        return false;
    }

    @PostMapping("/card/{number}/block")
    public String postBlockCard(@PathVariable String number, @RequestParam("password") String password) {

        try {
            cardService.blockCardByNumber(number, password);
        } catch (InvalidCardNumberException e) {
            log.warn("CLIENT {} tried block non-existent card [number: {}]"
                    , SecurityContextHolder.getContext().getAuthentication().getName(), number);
            return "redirect:/client/cards";
        } catch (AuthenticationFailedException e) {
            log.warn("CLIENT {} entered the wrong password trying block card [number: {}]"
                    , SecurityContextHolder.getContext().getAuthentication().getName(), number);
            return "redirect:/client/card/" + number + "/block?error";
        }

        log.info("CLIENT {} blocked card [number: {}]"
                , SecurityContextHolder.getContext().getAuthentication().getName(), number);
        return "redirect:/client/cards";
    }

    @GetMapping("/card/payment")
    public String getPayment(Model model) {
        List<Card> cardList = cardService.getCardListByCurrentUser().stream().filter(card -> !card.isBlocked()).collect(Collectors.toList());

        model.addAttribute("paymentDto", new PaymentDto());
        model.addAttribute("cardList", cardList);
        return "/client/payment";
    }

    @PostMapping("/card/unblock-request")
    public String postMakeRequest(@RequestParam("cardNumber") String cardNumber) {
        try {
            cardService.makeCardUnblockRequest(cardNumber);
        } catch (InvalidCardNumberException e) {
            log.warn("CLIENT {} tried to make an unlock request for non-existent card [number: {}]"
                    , SecurityContextHolder.getContext().getAuthentication().getName(), cardNumber);
            return "redirect:/client/cards";
        }
        log.info("CLIENT {} made an unlock request for card [number: {}]"
                , SecurityContextHolder.getContext().getAuthentication().getName(), cardNumber);
        return "redirect:/client/cards";
    }


    @PostMapping(value = "/card/payment")
    public String postPaymentSend(@RequestParam("action") String action, Model model, @ModelAttribute("paymentDto") @Valid PaymentDto paymentDto, BindingResult bindingResult) {
        List<Card> cardList = cardService.getCardListByCurrentUser().stream().filter(card -> !card.isBlocked()).collect(Collectors.toList());
        model.addAttribute("cardList", cardList);

        if (bindingResult.hasErrors()) {
            return "/client/payment";
        }

        try {
            if (action.equals("send")) {
                paymentService.makePayment(paymentDto);
                log.info("CLIENT {} made payment [number: {}] [number to: {}] [payment sum: {}]", SecurityContextHolder.getContext().getAuthentication().getName(), paymentDto.getCardDestinationNumber(), paymentDto.getCardDestinationNumber(), paymentDto.getMoney());
            } else {
                paymentService.createPreparedPayment(paymentDto);
                log.info("CLIENT {} prepared payment [number from: {}] [number to: {}] [payment sum: {}]", SecurityContextHolder.getContext().getAuthentication().getName(), paymentDto.getCardDestinationNumber(), paymentDto.getCardDestinationNumber(), paymentDto.getMoney());
            }
        } catch (InvalidCardNumberException e) {
            log.warn("CLIENT {} tried to make payment for card with wrong number [number: {}]", SecurityContextHolder.getContext().getAuthentication().getName(), paymentDto.getCardDestinationNumber());
            model.addAttribute("invalidCardNumber", true);
            return "/client/payment";
        } catch (InvalidBalanceOnCardException e) {
            log.warn("CLIENT {} tried to make payment with invalid balance [number: {}] [payment sum: {}]", SecurityContextHolder.getContext().getAuthentication().getName(), paymentDto.getCardSenderNumber(), paymentDto.getMoney());
            model.addAttribute("invalidBalance", true);
            return "/client/payment";
        } catch (BlockedCardException e) {
            log.warn("CLIENT {} tried to make payment for blocked card [number: {}]", SecurityContextHolder.getContext().getAuthentication().getName(), paymentDto.getCardDestinationNumber());
            model.addAttribute("blockedCard", true);
            return "/client/payment";
        }

        return "redirect:/client/cards";
    }

    @PostMapping(value = "/card/payment/confirm", params = "action=confirm")
    public String postPaymentConfirm(@RequestParam("paymentId") String paymentId, RedirectAttributes redirectAttributes) {
        try {
            paymentService.confirmPayment(Long.valueOf(paymentId));
        } catch (InvalidBalanceOnCardException e) {
            redirectAttributes.addAttribute("invalidBalance", paymentId);
            log.warn("CLIENT {} tried to confirm prepared payment for card with invalid balance[payment id: {}]"
                    , SecurityContextHolder.getContext().getAuthentication().getName(), paymentId);
            return "redirect:/client/payments";
        } catch (InvalidCardNumberException e) {
            log.warn("CLIENT {} tried to confirm prepared payment for for non-existent card [payment id: {}]"
                    , SecurityContextHolder.getContext().getAuthentication().getName(), paymentId);
            return "redirect:/client/payments";
        } catch (BlockedCardException e) {
            log.warn("CLIENT {} tried to confirm prepared payment for blocked card [payment id: {}]"
                    , SecurityContextHolder.getContext().getAuthentication().getName(), paymentId);
            redirectAttributes.addAttribute("blockedCard", paymentId);
            return "redirect:/client/payments";
        }

        log.info("CLIENT {} confirmed prepared payment [payment id: {}]"
                , SecurityContextHolder.getContext().getAuthentication().getName(), paymentId);
        return "redirect:/client/payments";
    }


}
