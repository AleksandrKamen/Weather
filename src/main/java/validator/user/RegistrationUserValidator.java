package validator.user;

import model.user.dto.UserDto;
import model.user.repository.UserRepository;
import validator.Error;
import validator.ValidationResult;
import validator.Validator;

public class RegistrationUserValidator implements Validator<UserDto> {
    private final UserRepository userRepository = new UserRepository();
    @Override
    public ValidationResult isValid(UserDto object) {
        var validationResult = new ValidationResult();

        //login
        if (!object.getLogin().matches("^[a-zA-Z0-9@-_]{3,20}$]")) {
            if (object.getLogin() == null || object.getLogin().isEmpty()) {
                validationResult.add(Error.of(400, "Имя пользователя не задано"));
            }
            if (object.getLogin().contains(" ")) {
                validationResult.add(Error.of(400, "Имя пользователя содержит пробелы"));
            }
            if (object.getLogin().length() < 3 ) {
                validationResult.add(Error.of(400, "Имя пользователя слишком короткое"));
            }
            if (object.getLogin().length() > 20) {
                validationResult.add(Error.of(400, "Имя пользователя слишком длинное"));
            }
        }
         if(userRepository.findByLoginWithSession(object.getLogin()).isPresent()) {
            validationResult.add(Error.of(409, "Пользователь с данным именем уже существует"));
        }

        //password
        if (!object.getPassword().matches("^[a-zA-Z0-9]{3,10}$")) {
            if (object.getPassword() == null || object.getPassword().isEmpty()) {
                validationResult.add(Error.of(400, "Пароль не задан"));
            }
            if (object.getPassword().contains(" ")) {
                validationResult.add(Error.of(400, "Пароль содержит пробелы"));
            }
            if (object.getPassword().length() < 3 || object.getPassword().length() > 10) {
                validationResult.add(Error.of(400, "Пароль не соответсвует необходимой длине"));
            }
            if (object.getPassword().replaceAll("[a-zA-Z\\d]", "").length() != 0) {
                validationResult.add(Error.of(400, "В пароле присутсвтуют спец символы"));
            }
        }
        if (object.getPassword().equals(object.getLogin())) {
            validationResult.add(Error.of(400, "Пароль не может совпадать с именем пользователя"));
        }
        if (!object.getPassword().equals(object.getConfirmPassword())){
            validationResult.add(Error.of(400, "Подтвердите пароль"));
        }
        return validationResult;
    }
}
