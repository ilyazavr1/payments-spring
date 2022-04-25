package ua.epma.paymentsspring.controller;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.epma.paymentsspring.model.entity.Card;
import ua.epma.paymentsspring.model.entity.Role;
import ua.epma.paymentsspring.model.entity.User;
import ua.epma.paymentsspring.model.excwption.AuthenticationFailedException;
import ua.epma.paymentsspring.model.excwption.InvalidCardNumberException;
import ua.epma.paymentsspring.model.service.CardService;
import ua.epma.paymentsspring.model.service.UserService;

import javax.servlet.http.HttpServletRequest;

@Log4j2
@AllArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {

    private UserService userService;
    private CardService cardService;


    @GetMapping("/users")
    public String getUsers(Model model) {

        model.addAttribute("userList", userService.getAllUsers());

        return "/admin/users";
    }

    @PostMapping("/user/{id}/block")
    public String getUsersBlock(Model model, @PathVariable String id) {
        User user = userService.blockUserById(Long.valueOf(id));
        if (user != null) log.info("ADMINISTRATOR {} blocked {}",
                SecurityContextHolder.getContext().getAuthentication().getName(), user.getEmail());
        else log.info("ADMINISTRATOR {} tried to block non-existent user [id: {}]",
                SecurityContextHolder.getContext().getAuthentication().getName(), id);
        return "redirect:/admin/users";
    }

    @PostMapping("/user/{id}/unblock")
    public String getUsersUnblock(Model model, @PathVariable String id) {
        User user = userService.unblockUserById(Long.valueOf(id));
        if (user != null) log.info("ADMINISTRATOR {} unblocked {}",
                SecurityContextHolder.getContext().getAuthentication().getName(), user.getEmail());
        else log.info("ADMINISTRATOR {} tried to unblock non-existent user [id: {}]",
                SecurityContextHolder.getContext().getAuthentication().getName(),id);
        return "redirect:/admin/users";
    }

    @GetMapping("/user/{id}/cards")
    public String getUsersCards(Model model, @PathVariable String id) {

        model.addAttribute("cardsList", cardService.getCardListByUserId(Long.valueOf(id)));

        return "/admin/userCards";
    }

    @GetMapping("/user/{userid}/card/{cardId}/block")
    public String getCardBlock(Model model, @PathVariable String cardId, @PathVariable String userid) {
        Card card = cardService.getCardById(Long.valueOf(cardId));

        if (card == null) return "redirect:/admin/user/" + userid + "/cards";

        model.addAttribute("card", card);

        return "/admin/adminBlockCard";
    }

    @GetMapping("/cards/unblock-request")
    public String getCardsUnblockRequest(Model model) {
        model.addAttribute("requestList", cardService.getCardUnblockRequest());
        return "/admin/userCardsUnblockRequests";
    }

    @PostMapping("/user/card/block")
    public String postCardBlock(@RequestParam("cardNumber") String cardNumber,
                                @RequestParam("cardId") String cardId,
                                @RequestParam("userId") String clientId,
                                @RequestParam("password") String password) {

        try {
            cardService.blockCardByNumber(cardNumber, password);
        } catch (InvalidCardNumberException e) {
            log.warn("ADMINISTRATOR {} tried to block non-existent card {}",
                    SecurityContextHolder.getContext().getAuthentication().getName(), cardNumber);
            return "redirect:/admin/user/" + clientId + "/cards";
        } catch (AuthenticationFailedException e) {
            log.warn("ADMINISTRATOR {} entered the wrong password trying block card {}",
                    SecurityContextHolder.getContext().getAuthentication().getName(), cardNumber);
            return "redirect:/admin/user/" + clientId + "/card/" + cardId + "/block?wrongPassword";
        }
        log.info("ADMINISTRATOR {} blocked card [card number: {}]",
                SecurityContextHolder.getContext().getAuthentication().getName(), cardNumber);
        return "redirect:/admin/user/" + clientId + "/cards";
    }


    @PostMapping("/user/card/unblock")
    public String postCardUnlock(@RequestParam("cardNumber") String cardNumber,
                                 @RequestParam("userId") String clientId,
                                 @RequestParam(value = "requestId", required = false) String requestId, HttpServletRequest request) {
        String referer;
        try {
            cardService.unblockCardByNumber(cardNumber);
            referer = request.getHeader("referer");
        } catch (InvalidCardNumberException e) {
            log.warn("ADMINISTRATOR {} tried to unblock non-existent card {}",
                    SecurityContextHolder.getContext().getAuthentication().getName(), cardNumber);
            return "redirect:/admin/user/" + clientId + "/cards";
        }

        if (referer.contains("unblock-request")) {
            if (!requestId.isEmpty()) {
                cardService.processedCardUnblockRequest(Long.valueOf(requestId));
                log.info("ADMINISTRATOR {} unblocked card [number: {}]"
                        , SecurityContextHolder.getContext().getAuthentication().getName(), cardNumber);
            }
            return "redirect:/admin/cards/unblock-request";
        }

        log.info("ADMINISTRATOR {} unblocked card [number: {}]"
                , SecurityContextHolder.getContext().getAuthentication().getName(), cardNumber);
        return "redirect:/admin/user/" + clientId + "/cards";
    }
}
