package service;

import model.user.dto.UserDto;
import model.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import validator.exception.ValidationException;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationUserTest {
    RegistrationService registrationService;
    UserRepository userRepository;
    @BeforeEach
    void init() {
        registrationService = new RegistrationService();
        userRepository = new UserRepository();
    }

    @AfterEach
    void drop() {
        registrationService = null;
        userRepository = null;
    }

    @ParameterizedTest
    @MethodSource("service.RegistrationUserTest#getRightNewUsers")
    void registrationSuccessful(UserDto newUser) {
        var newSession = registrationService.registration(newUser);
        assertTrue(userRepository.findByLoginWithSession(newUser.getLogin()).isPresent());
        assertNotNull(newSession);
    }

    @Nested
    class registrationFailed {
        @ParameterizedTest
        @MethodSource("service.RegistrationUserTest#getWrongNewUsers")
        void incorrectParameters(UserDto newUser) {
            assertThrows(ValidationException.class, () -> registrationService.registration(newUser));
        }

        @Test
        void userDuplication() {
            var newUser = UserDto.builder()
                    .login("test@test")
                    .password("test")
                    .confirmPassword("test")
                    .build();
            registrationService.registration(newUser);
            assertThrows(ValidationException.class, () -> registrationService.registration(newUser));
        }
    }

    static Stream<Arguments> getRightNewUsers() {
        return Stream.of(
                Arguments.of(UserDto.builder()
                        .login("test@test")
                        .password("test")
                        .confirmPassword("test")
                        .build()),
                Arguments.of(UserDto.builder()
                        .login("Terminator-1")
                        .password("test")
                        .confirmPassword("test")
                        .build()),
                Arguments.of(UserDto.builder()
                        .login("Логин!!!")
                        .password("test")
                        .confirmPassword("test")
                        .build())
        );
    }

    static Stream<Arguments> getWrongNewUsers() {
        return Stream.of(
                //Empty login
                Arguments.of(UserDto.builder()
                        .login("")
                        .password("test")
                        .confirmPassword("test")
                        .build()),
                //Wrong confirmPassword
                Arguments.of(UserDto.builder()
                        .login("Terminator-1")
                        .password("test")
                        .confirmPassword("test1")
                        .build()),
                //Short login
                Arguments.of(UserDto.builder()
                        .login("A")
                        .password("test")
                        .confirmPassword("test")
                        .build()),
                // Long login
                Arguments.of(UserDto.builder()
                        .login("A123AABBB456789BBBCCCDDD")
                        .password("test")
                        .confirmPassword("test")
                        .build()),
                // Login = password
                Arguments.of(UserDto.builder()
                        .login("test")
                        .password("test")
                        .confirmPassword("test")
                        .build()),
                // password has invalid characters
                Arguments.of(UserDto.builder()
                        .login("test")
                        .password("test!")
                        .confirmPassword("test!")
                        .build())
        );
    }


}