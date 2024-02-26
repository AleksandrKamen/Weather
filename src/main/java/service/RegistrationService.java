package service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.session.entity.Session;
import model.session.repository.SessionRepository;
import model.user.dto.UserDto;
import model.user.entity.User;
import model.user.mapper.UserMapper;
import model.user.repository.UserRepository;
import util.PropertiesUtil;
import validator.exception.ValidationException;
import validator.user.AuthorizationUserValidator;
import validator.user.RegistrationUserValidator;
import java.time.LocalDateTime;
import java.util.Arrays;

public class RegistrationService {
    private static final String SESSION_TIME_KEY = "session_time";
    private static final String SESSION_ID = "session_id";
    private  final UserRepository userRepository = new UserRepository();
    private  final SessionRepository sessionsRepository = new SessionRepository();
    private  final RegistrationUserValidator registrationUserValidator = new RegistrationUserValidator();
    private final AuthorizationUserValidator authorizationUserValidator = new AuthorizationUserValidator();

    public Session registration(UserDto userDto) {
        var validationResult = registrationUserValidator.isValid(userDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        var newUser = UserMapper.INSTANCE.userDtoToUser(userDto);
        var newSession = buildNewSession(newUser);
        newUser.getSessions().add(newSession);
        userRepository.save(newUser);
        return newSession;
       }

    public Session authorization(UserDto userDto) {
        var validationResult = authorizationUserValidator.isValid(userDto);
        if (!validationResult.isValid()){
            throw new ValidationException(validationResult.getErrors());
        }
        var user = userRepository.findByLoginWithSession(userDto.getLogin()).get();
        var newSession = buildNewSession(user);
        user.addSession(newSession);
        userRepository.update(user);
        return newSession;
    }

    public void setCookies(HttpServletResponse response, Session session){
        var sessionIdCookie = new Cookie(SESSION_ID, session.getId());
        response.addCookie(sessionIdCookie);
    }
    public void logOut(HttpServletRequest request, HttpServletResponse response) {
        var cookies = request.getCookies();
        var optionalCookie = Arrays.stream(cookies)
                .filter(cookie -> SESSION_ID.equals(cookie.getName()))
                .findFirst();
        if (optionalCookie.isPresent()){
            sessionsRepository.delete(optionalCookie.get().getValue());
            var deleteCookie = new Cookie(SESSION_ID, null);
            deleteCookie.setMaxAge(0);
            response.addCookie(deleteCookie);
        }
    }
    private static Session buildNewSession(User newUser) {
        var newSession = Session.builder()
                .user(newUser)
                .expiresAt(
                    LocalDateTime.now().plusMinutes(Long.parseLong(PropertiesUtil.get(SESSION_TIME_KEY)))
                )
                .build();
        return newSession;
    }
}
