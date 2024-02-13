package validator.user;

import model.users.dto.UserDto;
import model.users.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import validator.Error;
import validator.ValidationResult;
import validator.Validator;

public class AuthorizationUserValidator implements Validator<UserDto> {
    private final UserRepository userRepository = new UserRepository();
    @Override
    public ValidationResult isValid(UserDto object) {
        var validationResult = new ValidationResult();
        var mabyUser = userRepository.findByLogin(object.getLogin());
        if (mabyUser.isPresent()){
            if (!BCrypt.checkpw(object.getPassword(), mabyUser.get().getPassword())){
                validationResult.add(Error.of(400, "Пароль введен неверно"));
            }
        } else {
            validationResult.add(Error.of(404, "Пользователь с указанным именем не найден"));
        }
        return validationResult;
    }
}
