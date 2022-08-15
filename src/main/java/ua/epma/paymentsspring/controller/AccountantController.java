package ua.epma.paymentsspring.controller;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.epma.paymentsspring.service.CardService;
import ua.epma.paymentsspring.service.UserService;

@Log4j2
@AllArgsConstructor
@Controller
@RequestMapping("/accountant")
public class AccountantController {

    private UserService userService;
    private CardService cardService;


}
