package users.service;

import model.users.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import model.users.dto.UserDto;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserServiceTest {
    UserService userService;
    @BeforeEach
    void init(){
         userService = new UserService();
    }
    @AfterEach
    void drop(){
        userService = null;
    }

   @ParameterizedTest
   @MethodSource("users.service.UserServiceTest#getCorrectUserDto")
   void CorrectUserDto(UserDto userDto){
       var user = userService.createUser(userDto);
       assertEquals(user.getLogin(),userDto.getLogin());


   }

    static Stream<Arguments> getCorrectUserDto() {
        return Stream.of(
               Arguments.of(UserDto.builder().login("A@123er1").password("Aa12345688").build()),
               Arguments.of(UserDto.builder().login("AAA!6578s").password("Aa12345688").build()),
               Arguments.of(UserDto.builder().login("").password("").build())
        );
    }

}