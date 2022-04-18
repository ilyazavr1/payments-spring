package ua.epma.paymentsspring.model.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.epma.paymentsspring.model.dto.UserDto;
import ua.epma.paymentsspring.model.entity.Role;
import ua.epma.paymentsspring.model.entity.User;
import ua.epma.paymentsspring.model.excwption.UserAlreadyExistException;
import ua.epma.paymentsspring.model.repository.RoleRepository;
import ua.epma.paymentsspring.model.repository.UserRepository;


@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private PasswordEncoder encoder;

    public User getUserById(Long id) throws RuntimeException {
        User user = userRepository.findById(id).orElse(null);

        return user;
    }

    public User getUserByEmail(String email) throws RuntimeException {
        User user = userRepository.findByEmail(email);

        return user;
    }


    public User registerUser(UserDto userDto) throws UserAlreadyExistException {
        if (emailExists(userDto.getEmail())) throw new UserAlreadyExistException();

        User user = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .surname(userDto.getSurname())
                .email(userDto.getEmail())
                .password(encoder.encode(userDto.getPassword()))
                .blocked(false)
                .role(roleRepository.findByRoleEnum(Role.RoleEnum.CLIENT))
                .build();

        return userRepository.save(user);
    }


    private boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }


}
