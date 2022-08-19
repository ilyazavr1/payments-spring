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
import ua.epma.paymentsspring.exception.UserAlreadyExistException;
import ua.epma.paymentsspring.model.constants.UriPath;
import ua.epma.paymentsspring.model.dto.AddressDto;
import ua.epma.paymentsspring.model.dto.UserDto;
import ua.epma.paymentsspring.model.entity.Role;
import ua.epma.paymentsspring.model.entity.User;
import ua.epma.paymentsspring.service.CityService;
import ua.epma.paymentsspring.service.CountryService;
import ua.epma.paymentsspring.service.UserAddressService;
import ua.epma.paymentsspring.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Log4j2
@AllArgsConstructor
@Controller
public class RegistrationController {

    private UserService userService;
    private CountryService countryService;
    private CityService cityService;
    private UserAddressService userAddressService;

    @GetMapping("/select-country")
    public String getCountrySelection(Model model) {
        model.addAttribute("countries", countryService.getCountries());

        return "/selectCountry";
    }

    @PostMapping("/select-country")
    public String postCountrySelection(@ModelAttribute(name = "countryName") String countryName, HttpSession httpSession) {
        if (countryName == null || countryName.isEmpty() || countryService.getCountryByName(countryName) == null) {
            return "redirect:/select-country";
        }

        httpSession.setAttribute("countryName", countryName);
        return "redirect:/registration";
    }

    @GetMapping(UriPath.REGISTRATION)
    public String getRegistration(HttpSession httpSession, Model model) {

        String country = httpSession.getAttribute("countryName") == null
                ? null
                : httpSession.getAttribute("countryName").toString();

        if (country == null || country.isEmpty()) {
            return "redirect:/select-country";
        }

        model.addAttribute("userDto", new UserDto());
        model.addAttribute("addressDto", new AddressDto());
        model.addAttribute("cities", cityService.getCitiesByCountryName(country));

        return UriPath.REGISTRATION;
    }

    @PostMapping(UriPath.REGISTRATION)
    public String getRegistration(@ModelAttribute(name = "userDto") @Valid UserDto userDto,
                                  BindingResult bindingResultUserDto,
                                  @ModelAttribute(name = "addressDto") @Valid AddressDto addressDto,
                                  BindingResult bindingResultAddressDto,
                                  HttpSession httpSession, Model model) {
        String country = httpSession.getAttribute("countryName") == null
                ? null : httpSession.getAttribute("countryName").toString();

        addressDto.setCountryName(country);

        if (country == null || country.isEmpty()) {
            return "redirect:/select-country";
        }

        if (bindingResultUserDto.hasErrors() || bindingResultAddressDto.hasErrors()) {
            model.addAttribute("cities", cityService.getCitiesByCountryName(country));
            return UriPath.REGISTRATION;
        }

        User user;
//TODO clean up RegistrationController
        try {
            user = userService.registerUser(userDto);
            userAddressService.addAddressToUser(addressDto, user);

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
