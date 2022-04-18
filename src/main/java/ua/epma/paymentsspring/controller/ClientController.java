package ua.epma.paymentsspring.controller;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ua.epma.paymentsspring.model.entity.Card;
import ua.epma.paymentsspring.model.excwption.BlockedCardException;
import ua.epma.paymentsspring.model.excwption.InvalidCardName;
import ua.epma.paymentsspring.model.excwption.InvalidCardNumberException;
import ua.epma.paymentsspring.model.excwption.InvalidMoneyAmountException;
import ua.epma.paymentsspring.model.service.CardService;
import ua.epma.paymentsspring.model.service.UserService;

@AllArgsConstructor
@Controller
@RequestMapping("/client")
public class ClientController {

    private final UserService userService;
    private final CardService cardService;


    @GetMapping("/cards")
    public String getCards(Model model) {
        model.addAttribute("cardList", cardService.getCardListByUser());

        return "/client/cards";
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
            return "redirect:/client/card/create?error";
        }

        return "redirect:/client/cards";
    }

    /*@GetMapping("/card/{number}/top-up")
    public String getCardTopUp(@RequestParam(name = "cardNumber", required = false) String cardNumber, Model model) {
        if (cardNumber == null || cardNumber.isEmpty()) return "redirect:/client/cards";
        Card card;
        try {
            card= cardService.getCardOfCurrentUserByNumber(cardNumber);
            if (card.isBlocked())  return "redirect:/client/cards";
            model.addAttribute("invalidMoney", model.getAttribute("invalidMoney"));
            model.addAttribute("card", card);
        } catch (InvalidCardNumberException e) {
            return "redirect:/client/cards";
        }

        return "/client/cardTopUp";
    }*/
    @GetMapping("/card/{number}/top-up")
    public String getCardTopUp(@PathVariable String number, Model model) {
        if (number == null || number.isEmpty()) return "redirect:/client/cards";
        Card card;
        try {
            card = cardService.getCardOfCurrentUserByNumber(number);
            if (card.isBlocked()) return "redirect:/client/cards";
            model.addAttribute("card", card);
        } catch (InvalidCardNumberException e) {
            return "redirect:/client/cards";
        }

        return "/client/cardTopUp";
    }

    @PostMapping("/card/{number}/top-up")
    public String postCardTopUp(@RequestParam("money") String money, ModelMap model, @PathVariable String number) {
        try {
            cardService.updateCardWithMoney(number, money);
        } catch (BlockedCardException e) {
            return "redirect:/client/cards";
        } catch (InvalidMoneyAmountException e) {
            return "redirect:/client/card/" + number + "/top-up?error";
        }
        return "redirect:/client/cards";
    }
  /*  @PostMapping("/card/top-up")
    public ModelAndView postCardTopUp(@RequestParam("money") String money, @RequestParam("cardNumber") String cardNumber, ModelMap model) {
        try {
            cardService.updateCardWithMoney(cardNumber, money);
        } catch (BlockedCardException e) {
            return new ModelAndView("redirect:/client/cards", model);
        } catch (InvalidMoneyAmountException e) {
            model.addAttribute("invalidMoney", true);
            model.addAttribute("cardNumber", cardNumber);
            return new ModelAndView("redirect:/client/card/top-up", model);
        }
        return new ModelAndView("redirect:/client/cards", model);
    }*/


    @GetMapping("/card/{number}/block")
    public String getCardBlock(@PathVariable String number, Model model) {
        if (number == null || number.isEmpty()) return "redirect:/client/cards";
        Card card;
        try {
            card = cardService.getCardOfCurrentUserByNumber(number);
            if (card.isBlocked()) return "redirect:/client/cards";
            model.addAttribute("card", card);
        } catch (InvalidCardNumberException e) {
            return "redirect:/client/cards";
        }

        return "/client/blockCard";
    }

    @PostMapping("/card/{number}/block")
    public String postBlockCard(@PathVariable String number,@RequestParam("password") String password){


        return "";
    }

}
