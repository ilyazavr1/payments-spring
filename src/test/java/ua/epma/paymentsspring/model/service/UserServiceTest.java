package ua.epma.paymentsspring.model.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ua.epma.paymentsspring.model.dto.UserDto;
import ua.epma.paymentsspring.model.entity.Role;
import ua.epma.paymentsspring.model.entity.User;
import ua.epma.paymentsspring.model.exception.UserAlreadyExistException;
import ua.epma.paymentsspring.model.repository.RoleRepository;
import ua.epma.paymentsspring.model.repository.UserRepository;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {


    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    private final static Long ID = 1L;
    private final static String FIRST_NAME = "TEST_NAME";
    private final static String LAST_NAME = "TEST_LAST_NAME";
    private final static String SURNAME = "TEST_SURNAME";
    private final static String REGISTERED_EMAIL = "test@gmail.con";
    private final static String PASSWORD = "Qwerty12345";
    private final static String NOT_REGISTERED_EMAIL = "qweqwqweqwe@gmail.con";
    private final static String RANDOM_PASSWORD = "Qwerty12fas345";
    private final static String HASHED_PASSWORD = "$2a$10$GJwpUE5I3ZSYaPsgpamkN.CO8GDjFDB7HjDVBC6vXyo0kmwkIqqUS";

    private User REGISTERED_USER;
    private UserDto DTO_USER;
    private final static Role CLIENT_ROLE = Role.builder().roleEnum(Role.RoleEnum.CLIENT).build();

    @BeforeEach
    void setUp() {
        REGISTERED_USER = User.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .patronymic(SURNAME)
                .email(REGISTERED_EMAIL)
                .password(HASHED_PASSWORD)
                .role(CLIENT_ROLE)
                .blocked(false)
                .build();
        DTO_USER = UserDto.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .patronymic(SURNAME)
                .email(REGISTERED_EMAIL)
                .password(PASSWORD)
                .build();
    }

    @Test
    void registerUserThrowsUserAlreadyExistException() {
        when(userRepository.findByEmail(REGISTERED_EMAIL)).thenReturn(REGISTERED_USER);

        assertThrows(UserAlreadyExistException.class, () -> userService.registerUser(DTO_USER));
    }

    @Test
    void registerUserReturnSavedUser() throws UserAlreadyExistException {
        when(userRepository.findByEmail(REGISTERED_EMAIL)).thenReturn(null);
        when(userRepository.save(REGISTERED_USER)).thenReturn(REGISTERED_USER);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(HASHED_PASSWORD);
        when(roleRepository.findByRoleEnum(Role.RoleEnum.CLIENT)).thenReturn(CLIENT_ROLE);

        assertEquals(userService.registerUser(DTO_USER), REGISTERED_USER);
        verify(userRepository, times(1)).save(REGISTERED_USER);
    }

    @Test
    void unblockUserByIdDoNotUnblock() {
        when(userRepository.findById(ID)).thenReturn(Optional.empty());

        userService.unblockUserById(ID);
        verify(userRepository, times(0)).save(REGISTERED_USER);
    }


    @Test
    void blockUserByIdDoNotBlock() {
        when(userRepository.findById(ID)).thenReturn(Optional.empty());

        userService.blockUserById(ID);
        verify(userRepository, times(0)).save(REGISTERED_USER);
    }

    @Test
    void blockUserByIdUnblock() {
        when(userRepository.findById(ID)).thenReturn(Optional.of(REGISTERED_USER));

        userService.unblockUserById(ID);
        verify(userRepository, times(1)).save(REGISTERED_USER);
    }

    @Test
    void blockUserByIdBlock() {
        when(userRepository.findById(ID)).thenReturn(Optional.of(REGISTERED_USER));

        userService.blockUserById(ID);
        verify(userRepository, times(1)).save(REGISTERED_USER);
    }
}