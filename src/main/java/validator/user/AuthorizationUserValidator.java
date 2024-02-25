package validator.user;

import model.user.dto.UserDto;
import model.user.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import validator.Error;
import validator.ValidationResult;
import validator.Validator;

public class AuthorizationUserValidator implements Validator<UserDto> {
    private final UserRepository userRepository = new UserRepository();
    @Override
    public ValidationResult isValid(UserDto object) {
        var validationResult = new ValidationResult();
        var mabyUser = userRepository.findByLoginWithSession(object.getLogin());
        if (!mabyUser.isPresent() || !BCrypt.checkpw(object.getPassword(), mabyUser.get().getPassword())){
          validationResult.add(Error.of(400, "Пользователь с указанным именем не найден или пароль введен неверно"));
        }
        return validationResult;
    }
}
