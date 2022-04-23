package ua.epma.paymentsspring.controller;


import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.epma.paymentsspring.model.entity.Card;
import ua.epma.paymentsspring.model.entity.Role;
import ua.epma.paymentsspring.model.excwption.AuthenticationFailedException;
import ua.epma.paymentsspring.model.excwption.InvalidCardNumberException;
import ua.epma.paymentsspring.model.service.CardService;
import ua.epma.paymentsspring.model.service.UserService;

import javax.servlet.http.HttpServletRequest;

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

        userService.blockUserById(Long.valueOf(id));

        return "redirect:/admin/users";
    }

    @PostMapping("/user/{id}/unblock")
    public String getUsersUnblock(Model model, @PathVariable String id) {
        System.out.println(id);
        userService.unblockUserById(Long.valueOf(id));

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
            return "redirect:/admin/user/" + clientId + "/cards";
        } catch (AuthenticationFailedException e) {
            return "redirect:/admin/user/" + clientId + "/card/" + cardId + "/block?wrongPassword";
        }
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
            return "redirect:/admin/user/" + clientId + "/cards";
        }
        System.out.println(referer);
        if (referer.contains("unblock-request")) {
            if (!requestId.isEmpty()) {
                cardService.processedCardUnblockRequest(Long.valueOf(requestId));
            }
            return "redirect:/admin/cards/unblock-request";
        }


        return "redirect:/admin/user/" + clientId + "/cards";
    }
}
