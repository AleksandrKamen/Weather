package validator.user;

import model.users.dto.UserDto;
import model.users.repository.UserRepository;
import validator.Error;
import validator.ValidationResult;
import validator.Validator;

public class RegistrationUserValidator implements Validator<UserDto> {
    private final UserRepository userRepository = new UserRepository();
    @Override
    public ValidationResult isValid(UserDto object) {
        var validationResult = new ValidationResult();

        //login
        if (!object.getLogin().matches("^[a-zA-Z0-9@-_]{3,10}$]")) {
            if (object.getLogin() == null || object.getLogin().isEmpty()) {
                validationResult.add(Error.of(400, "Логин не задан"));
            }
            if (object.getLogin().contains(" ")) {
                validationResult.add(Error.of(400, "Логин содержит пробелы"));
            }
            if (object.getLogin().length() < 3 ) {
                validationResult.add(Error.of(400, "Логин млишком короткий"));
            }
            if (object.getLogin().length() > 10) {
                validationResult.add(Error.of(400, "Логин слишком длинный"));
            }
        }
         if(userRepository.findByLogin(object.getLogin()).isPresent()) {
            validationResult.add(Error.of(409, "Пользователь с данным логином уже существует"));
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
            validationResult.add(Error.of(400, "Пароль не может совпадать с логином"));
        }
        if (!object.getPassword().equals(object.getConfirmPassword())){
            validationResult.add(Error.of(400, "Подтверждение пароля не совпадает с первоначальным паролем"));
        }
        return validationResult;
    }
}
