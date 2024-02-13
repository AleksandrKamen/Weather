package service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.session.entity.SessionEntity;
import model.session.repository.SessionsRepository;
import model.users.dto.UserDto;
import model.users.entity.UsersEntity;
import model.users.mapper.CreateUserMapper;
import model.users.repository.UserRepository;
import util.PropertiesUtil;
import validator.ValidationResult;
import validator.exception.ValidationException;
import validator.user.AuthorizationUserValidator;
import validator.user.RegistrationUserValidator;
import java.time.LocalDateTime;
import java.util.Arrays;

public class RegistrationService {
    private static final String SESSION_TIME_KEY = "session_time";
    private static final String SESSION_ID = "session_id";
    private  final UserRepository userRepository = new UserRepository();
    private  final SessionsRepository sessionsRepository = new SessionsRepository();
    private  final CreateUserMapper creatUserMapper = new CreateUserMapper();
    private  final RegistrationUserValidator registrationUserValidator = new RegistrationUserValidator();

    private final AuthorizationUserValidator authorizationUserValidator = new AuthorizationUserValidator();

    public SessionEntity registration(UserDto userDto) {
        var validationResult = registrationUserValidator.isValid(userDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        var newUser = creatUserMapper.mapFrom(userDto);
        var newSession = buildNewSession(newUser);
        newUser.getSessions().add(newSession);
        userRepository.save(newUser);
        return newSession;
       }

    public SessionEntity authorization(UserDto userDto) {
        var validationResult = authorizationUserValidator.isValid(userDto);
        if (!validationResult.isValid()){
            throw new ValidationException(validationResult.getErrors());
        }
        var user = userRepository.findByLogin(userDto.getLogin()).get();
        var newSession = buildNewSession(user);
        user.addSession(newSession);
        userRepository.update(user);
        return newSession;
    }

    public void setCookies(HttpServletRequest request, HttpServletResponse response, SessionEntity session){
        var cookies = request.getCookies();
        if (cookies==null || Arrays.stream(cookies)
                .filter(cookie -> SESSION_ID.equals(cookie.getName()))
                .findFirst()
                .isEmpty()){
            var sessionIdCookie = new Cookie(SESSION_ID, session.getId());
            response.addCookie(sessionIdCookie);
        }
    }

    public void logOut(HttpServletRequest request, HttpServletResponse response) {
        var cookies = request.getCookies();
        var mabeSession = Arrays.stream(cookies).filter(cookie -> SESSION_ID.equals(cookie.getName()))
                .findFirst();
        if (mabeSession.isPresent()){
            sessionsRepository.delete(mabeSession.get().getValue());
            var deleteCookie = new Cookie(SESSION_ID, null);
            deleteCookie.setMaxAge(0);
            response.addCookie(deleteCookie);
        }
    }

    private static SessionEntity buildNewSession(UsersEntity newUser) {
        var newSession = SessionEntity.builder()
                .user(newUser)
                .expiresat(LocalDateTime.now().plusMinutes(Integer.parseInt(PropertiesUtil.get(SESSION_TIME_KEY))))
                .build();
        return newSession;
    }
}
