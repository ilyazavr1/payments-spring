package ua.epma.paymentsspring.controller;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.epma.paymentsspring.exception.AuthenticationFailedException;
import ua.epma.paymentsspring.exception.InvalidCardNumberException;
import ua.epma.paymentsspring.model.entity.Card;
import ua.epma.paymentsspring.service.CardService;
import ua.epma.paymentsspring.service.UserService;

import javax.servlet.http.HttpServletRequest;

@Log4j2
@AllArgsConstructor
@Controller
@RequestMapping("/accountant")
public class AccountantController {

    private UserService userService;
    private CardService cardService;


    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("userList",
                userService.getAllUsersDtoWithNoAddressNoPasswordWhereRoleClient());
        return "accountant/users";
    }


    @GetMapping("/user/{id}/cards")
    public String getUsersCards(Model model, @PathVariable String id) {

        model.addAttribute("cardsList",
                cardService.getCardListByUserId(Long.valueOf(id)));

        return "accountant/userCards";
    }

    @GetMapping("/user/{userid}/card/{cardId}/block")
    public String getCardBlock(Model model, @PathVariable String cardId,
                               @PathVariable String userid) {
        Card card = cardService.getCardById(Long.valueOf(cardId));

        if (card == null) {
            return "redirect:/accountant/user/" + userid + "/cards";
        }

        model.addAttribute("card", card);

        return "accountant/accountantBlockCard";
    }


    @PostMapping("/user/card/block")
    public String postCardBlock(@RequestParam("cardNumber") String cardNumber,
                                @RequestParam("cardId") String cardId,
                                @RequestParam("userId") String clientId,
                                @RequestParam("password") String password) {

        try {
            cardService.blockCardByNumber(cardNumber, password);
        } catch (InvalidCardNumberException e) {
            log.warn("ACCOUNTANT {} tried to block non-existent card {}",
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    cardNumber);
            return "redirect:/accountant/user/" + clientId + "/cards";
        } catch (AuthenticationFailedException e) {
            log.warn(
                    "ACCOUNTANT {} entered the wrong password trying block " + "card {}",
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    cardNumber);
            return "redirect:/accountant/user/" + clientId + "/card/" + cardId + "/block?wrongPassword";
        }
        log.info("ACCOUNTANT     {} blocked card [card number: {}]",
                SecurityContextHolder.getContext().getAuthentication().getName(),
                cardNumber);
        return "redirect:/accountant/user/" + clientId + "/cards";
    }

    @PostMapping("/user/card/unblock")
    public String postCardUnlock(@RequestParam("cardNumber") String cardNumber,
                                 @RequestParam("userId") String clientId,
                                 @RequestParam(value = "requestId", required
                                         = false) String requestId,
                                 HttpServletRequest request) {
        String referer;
        try {
            cardService.unblockCardByNumber(cardNumber);
            referer = request.getHeader("referer");
        } catch (InvalidCardNumberException e) {
            log.warn("ACCOUNTANT {} tried to unblock non-existent card {}",
                    SecurityContextHolder.getContext().getAuthentication().getName(),
                    cardNumber);
            return "redirect:/accountant/user/" + clientId + "/cards";
        }


        if (referer.contains("unblock-request")) {
            if (!requestId.isEmpty()) {
                cardService.processedCardUnblockRequest(
                        Long.valueOf(requestId));
                log.info("ACCOUNTANT {} unblocked card [number: {}]",
                        SecurityContextHolder.getContext().getAuthentication().getName(),
                        cardNumber);
            }
            return "redirect:/accountant/cards/unblock-request";
        }

        log.info("ACCOUNTANT {} unblocked card [number: {}]",
                SecurityContextHolder.getContext().getAuthentication().getName(),
                cardNumber);
        return "redirect:/accountant/user/" + clientId + "/cards";
    }

    @GetMapping("/cards/unblock-request")
    public String getCardsUnblockRequest(Model model) {
        model.addAttribute("requestList", cardService.getCardUnblockRequest());
        return "accountant/userCardsUnblockRequests";
    }

}
