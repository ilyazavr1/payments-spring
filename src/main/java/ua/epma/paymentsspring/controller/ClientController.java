package ua.epma.paymentsspring.controller;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.epma.paymentsspring.model.excwption.InvalidCardName;
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
        model.addAttribute("cardList" , cardService.getCardListByUser());

        return "/client/cards";
    }

    @GetMapping("/card/create")
    public String getCardCreate() {



        return "/client/createCard";
    }

    @PostMapping("/card/create")
    public String postCardCreate(@RequestParam(name = "name") String name) {
        System.out.println(name);

        try {
            cardService.createCard(name);
        } catch (InvalidCardName e) {
            return "redirect:/card/create?error";
        }


        return "/client/createCard";
    }


}
