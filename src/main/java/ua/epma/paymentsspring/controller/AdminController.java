package ua.epma.paymentsspring.controller;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.epma.paymentsspring.model.entity.User;
import ua.epma.paymentsspring.service.CardService;
import ua.epma.paymentsspring.service.UserService;

@Log4j2
@AllArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {


    private UserService userService;
    private CardService cardService;


    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("userList", userService.getAllUsersDtoWithAddressNoPasswordWhereRoleClient());
        return "admin/users";
    }

    @PostMapping("/user/{id}/block")
    public String getUsersBlock(Model model, @PathVariable String id) {
        User user = userService.blockUserById(Long.valueOf(id));
        if (user != null) {
            log.info("ADMINISTRATOR {} blocked {}", SecurityContextHolder.getContext().getAuthentication().getName(), user.getEmail());
        } else {
            log.info("ADMINISTRATOR {} tried to block non-existent user [id: {}]", SecurityContextHolder.getContext().getAuthentication().getName(), id);
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/user/{id}/unblock")
    public String getUsersUnblock(Model model, @PathVariable String id) {
        User user = userService.unblockUserById(Long.valueOf(id));
        if (user != null) {
            log.info("ADMINISTRATOR {} unblocked {}", SecurityContextHolder.getContext().getAuthentication().getName(), user.getEmail());
        } else {
            log.info("ADMINISTRATOR {} tried to unblock non-existent user [id: {}]", SecurityContextHolder.getContext().getAuthentication().getName(), id);
        }
        return "redirect:/admin/users";
    }

}
