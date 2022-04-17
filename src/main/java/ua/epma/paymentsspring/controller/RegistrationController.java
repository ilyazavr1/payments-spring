package ua.epma.paymentsspring.controller;


import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.epma.paymentsspring.model.constants.UriPath;
import ua.epma.paymentsspring.model.dto.UserDto;
import ua.epma.paymentsspring.model.entity.Role;
import ua.epma.paymentsspring.model.entity.User;
import ua.epma.paymentsspring.model.excwption.UserAlreadyExistException;
import ua.epma.paymentsspring.model.service.UserService;


import javax.validation.Valid;

@AllArgsConstructor
@Controller
public class RegistrationController {

    UserService userService;


    @GetMapping(UriPath.REGISTRATION)
    public String getRegistration(Model model) {
        model.addAttribute("userDto", new UserDto());

        User user = userService.getUserById(1L);

        if (user != null) {
            model.addAttribute("user", user);
        }

        return UriPath.REGISTRATION;
    }


    @PostMapping(UriPath.REGISTRATION)
    public String getRegistration(@ModelAttribute(name = "userDto") @Valid UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return UriPath.REGISTRATION;
        }

        User user;
        try {
            user = userService.registerUser(userDto);
            System.out.println("POST - " + user);
        } catch (UserAlreadyExistException e) {
            e.printStackTrace();
        }

        return UriPath.LOGIN;
    }

    @GetMapping(UriPath.LOGIN)
    public String getLogin(@RequestParam(value = "user", defaultValue = "false") String loginError, Model model) {

        return UriPath.LOGIN;
    }

    @GetMapping(UriPath.LOGIN_REDIRECT)
    public String redirect(@RequestParam(value = "user", defaultValue = "false") String loginError,Authentication authentication) {
        if (authentication.getAuthorities().toString().contains(Role.RoleEnum.CLIENT.name())) return "redirect:" + UriPath.CLIENT_CARDS;
        if (authentication.getAuthorities().toString().contains(Role.RoleEnum.ADMINISTRATOR.name())) return "redirect:" + UriPath.ADMIN_TEST;

        return UriPath.LOGIN;
    }

}
