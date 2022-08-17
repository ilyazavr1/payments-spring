package ua.epma.paymentsspring.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.epma.paymentsspring.model.dto.UserDto;
import ua.epma.paymentsspring.model.dto.UserDtoWithAddress;
import ua.epma.paymentsspring.model.dto.UserTest;
import ua.epma.paymentsspring.model.entity.Role;
import ua.epma.paymentsspring.model.entity.User;
import ua.epma.paymentsspring.exception.UserAlreadyExistException;
import ua.epma.paymentsspring.model.repository.RoleRepository;
import ua.epma.paymentsspring.model.repository.UserRepository;

import java.util.List;
import java.util.Optional;


/**
 * Service for business logic related with User.
 *
 * @author Illia
 */
@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;


    public List<User> getAllUsers() {
        System.out.println("---------------------------------------------------------------------------------------------");
/*

        System.out.println(userRepository.findUsersTest().get(3).getFirstName());
        System.out.println(userRepository.findUsersTest().get(3).getAddressId().getBuilding());

        List<UserTest> s = userRepository.findUsersTest();
        System.out.println(s.get(1));
*/

        System.out.println(userRepository.testFor());

        return userRepository.getUsersWithRoleClient();
    }

    public List<UserDtoWithAddress> getAllUsersDtoWithAddress() {


        return null;
    }
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User blockUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setBlocked(true);
            return userRepository.save(user.get());
        }
        return null;
    }

    public User unblockUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setBlocked(false);
            return userRepository.save(user.get());
        }
        return null;
    }

    /**
     * @param userDto UserDto object that contains validated information for creating User.
     * @return saved User in database.
     * @throws UserAlreadyExistException if email already exists in database.
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public User registerUser(UserDto userDto) throws UserAlreadyExistException {
        if (emailExists(userDto.getEmail())) {
            throw new UserAlreadyExistException();
        }

        User user = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .patronymic(userDto.getPatronymic())
                .email(userDto.getEmail())
                .password(encoder.encode(userDto.getPassword()))
                .blocked(false)
                .role(roleRepository.findByRoleEnum(Role.RoleEnum.CLIENT))
                .build();

        return userRepository.save(user);
    }


    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }


}