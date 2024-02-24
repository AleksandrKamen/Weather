package service;

import model.session.repository.SessionRepository;
import model.user.dto.UserDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import validator.exception.ValidationException;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthorizationUserTest {
    private RegistrationService registrationService;
    private SessionRepository sessionRepository;
    private static String LOGIN_TEST = "login";
    private static String PASSWORD_TEST = "password";

    @BeforeAll
     void init() {
        registrationService = new RegistrationService();
        sessionRepository = new SessionRepository();
    }
    @Test
    void authorizationSuccessful() {
        var user = UserDto.builder().login(LOGIN_TEST).password(PASSWORD_TEST).confirmPassword(PASSWORD_TEST).build();
        registrationService.registration(user);
        var newSession = registrationService.authorization(user);
      assertAll(
              ()-> assertNotNull(newSession),
              ()-> assertTrue(sessionRepository.findById(newSession.getId()).isPresent())
      );
    }

    @Test
    void incorrectPassword() {
        var user = UserDto.builder().login(LOGIN_TEST).password("123").build();
        assertThrows(ValidationException.class, () -> registrationService.authorization(user));
    }

    @Test
    void incorrectLogin() {
        var user = UserDto.builder().login(PASSWORD_TEST).password(PASSWORD_TEST).build();
        assertThrows(ValidationException.class, () -> registrationService.authorization(user));
    }


}
