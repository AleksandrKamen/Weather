package service;

import model.session.entity.SessionEntity;
import model.session.repository.SessionsRepository;
import model.users.dto.UserDto;
import model.users.entity.UsersEntity;
import model.users.mapper.CreateUserMapper;
import model.users.repository.UserRepository;
import org.hibernate.Hibernate;
import org.mindrot.jbcrypt.BCrypt;
import util.PropertiesUtil;
import validator.exception.ValidationException;
import validator.user.CreateUserValidator;

import java.time.LocalDateTime;

public class RegistrationService {
    private static final String SESSION_TIME_KEY = "session_time";
    private static final UserRepository userRepository = new UserRepository();
    private static final SessionsRepository sessionsRepository = new SessionsRepository();

    private static final CreateUserMapper creatUserMapper = new CreateUserMapper();
    private static final CreateUserValidator createUserValidator = new CreateUserValidator();

    public SessionEntity registration(UserDto userDto) {
        var validationResult = createUserValidator.isValid(userDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        var newUser = creatUserMapper.mapFrom(userDto);
        var newSession = SessionEntity.builder()
                .user(newUser)
                .expiresat(LocalDateTime.now().plusMinutes(Integer.parseInt(PropertiesUtil.get(SESSION_TIME_KEY))))
                .build();
        newUser.getSessions().add(newSession);
        userRepository.save(newUser);
        return newSession;
        // TODO: 18.01.2024 Валидация на наличие в БД
    }

    public SessionEntity authorization(UserDto userDto) {
        var validationResult = createUserValidator.isValid(userDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        var user = userRepository.findByLogin(userDto.getLogin());
        if (!user.isPresent() || !BCrypt.checkpw(userDto.getPassword(), user.get().getPassword())) {
            throw new IllegalArgumentException();
        }
        var newSession = SessionEntity.builder()
                .user(user.get())
                .expiresat(LocalDateTime.now().plusMinutes(Integer.parseInt(PropertiesUtil.get(SESSION_TIME_KEY))))
                .build();
         user.get().addSession(newSession);
         sessionsRepository.save(newSession);
        return newSession;
    }


}
