package service;

import model.user.dto.UserDto;
import model.user.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import validator.exception.ValidationException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthorizationUserTest {
    static RegistrationService registrationService = new RegistrationService();
    static UserRepository userRepository = new UserRepository();
    static String LOGIN = "login";
    static String PASSWORD = "password";

    @BeforeAll
    static void addUser() {
        registrationService.registration(UserDto.builder().login(LOGIN).password(PASSWORD).confirmPassword(PASSWORD).build());
    }

    @AfterAll
    static void destroy() {
        userRepository.delete(1L);
        registrationService = null;
        userRepository = null;
    }

    @Test
    void authorizationSuccessful() {
        var user = UserDto.builder().login(LOGIN).password(PASSWORD).build();
        var newSession = registrationService.authorization(user);
        assertTrue(newSession != null);
    }

    @Test
    void incorrectPassword() {
        var user = UserDto.builder().login(LOGIN).password("123").build();
        assertThrows(ValidationException.class, () -> registrationService.authorization(user));
    }

    @Test
    void incorrectLogin() {
        var user = UserDto.builder().login(PASSWORD).password(PASSWORD).build();
        assertThrows(ValidationException.class, () -> registrationService.authorization(user));
    }


}
