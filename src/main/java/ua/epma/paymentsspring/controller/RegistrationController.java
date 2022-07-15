package ua.epma.paymentsspring.controller;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
import ua.epma.paymentsspring.model.exception.UserAlreadyExistException;
import ua.epma.paymentsspring.model.service.UserService;


import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Log4j2
@AllArgsConstructor
@Controller
public class RegistrationController {

    UserService userService;


    @GetMapping(UriPath.REGISTRATION)
    public String getRegistration(Model model) {
        model.addAttribute("userDto", new UserDto());

        return UriPath.REGISTRATION;
    }


    @PostMapping(UriPath.REGISTRATION)
    public String getRegistration(@ModelAttribute(name = "userDto") @Valid UserDto userDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return UriPath.REGISTRATION;
        }

        User user;
        try {
            user = userService.registerUser(userDto);
        } catch (UserAlreadyExistException e) {
            log.warn("Email {} is registered", userDto.getEmail());
            model.addAttribute("emailExists", "emailExists");
            return UriPath.REGISTRATION;
        }
        log.warn("User registered with email: {}", user.getEmail());
        return UriPath.LOGIN;
    }

    @GetMapping(UriPath.LOGIN)
    public String getLogin(@RequestParam(value = "user", defaultValue = "false") String loginError, Model model) {

        return UriPath.LOGIN;
    }

    @GetMapping(UriPath.LOGIN_REDIRECT)
    public String redirect(@RequestParam(value = "user", defaultValue = "false") String loginError, Authentication authentication, HttpSession session) {

        session.setAttribute("user", userService.findUserByEmail(authentication.getName()));

        if (authentication.getAuthorities().toString().contains(Role.RoleEnum.CLIENT.name())) {
            log.info("{} logged in with email: {}", Role.RoleEnum.CLIENT.name(), authentication.getName());
            return "redirect:" + UriPath.CLIENT_CARDS;
        }

        if (authentication.getAuthorities().toString().contains(Role.RoleEnum.ADMINISTRATOR.name())) {
            log.info("{} logged in with email: {}", Role.RoleEnum.ADMINISTRATOR.name(), authentication.getName());
            return "redirect:" + UriPath.ADMIN_TEST;
        }

        return UriPath.LOGIN;
    }

}
