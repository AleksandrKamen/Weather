package validator.user;

import users.dto.UserDto;
import users.repository.UserRepository;
import validator.Error;
import validator.ValidationResult;
import validator.Validator;

public class CreateUserValidator implements Validator<UserDto> {
    private final UserRepository userRepository = new UserRepository();

    @Override
    public ValidationResult isValid(UserDto object) {
        var validationResult = new ValidationResult();

        //login
        if (!object.getLogin().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,30}$\n")) {
            if (object.getLogin() == null || object.getLogin().isEmpty()) {
                validationResult.add(Error.of(400, "Логин не задан"));
            }
            if (object.getLogin().length() < 6 || object.getLogin().length() > 30) {
                validationResult.add(Error.of(400, "Логин не соответсвует необходимой длине"));
            }
            if (object.getLogin().replaceAll("[a-zA-Z]", " ").equals(object.getLogin())) {
                validationResult.add(Error.of(400, "В Логине отсутствуют буквы"));
            }
            if (object.getLogin().replaceAll("[0-9]", "").equals(object.getLogin())) {
                validationResult.add(Error.of(400, "В Логине отсутствуют цифры"));
            }
            if (object.getLogin().toLowerCase().equals(object.getLogin())) {
                validationResult.add(Error.of(400, "В Логине отсутствуют заглавные буквы"));
            }
            if (object.getLogin().toUpperCase().equals(object.getLogin())) {
                validationResult.add(Error.of(400, "В Логине отсутствуют прописные буквы"));
            }
            if (object.getLogin().replaceAll("[a-zA-Z\\d]", "").length() == 0) {
                validationResult.add(Error.of(400, "В Логине отсутствуют спец символы"));
            }
        } else if(userRepository.findByLogin(object.getLogin()).isPresent()) {
            validationResult.add(Error.of(400, "Пользователь с данным логином уже существует"));
        }

        //password
        if (!object.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)(?=.*[@#$%^&+=])(?!.*\\\\s).{6,30}$")) {
            if (object.getPassword() == null || object.getPassword().isEmpty()) {
                validationResult.add(Error.of(400, "Пароль не задан"));
            }
            if (object.getPassword().contains(" ")) {
                validationResult.add(Error.of(400, "Пароль содержит пробелы"));
            }
            if (object.getPassword().length() < 6 || object.getPassword().length() > 30) {
                validationResult.add(Error.of(400, "Пароль не соответсвует необходимой длине"));
            }
            if (object.getPassword().replaceAll("[a-zA-Z]", "").equals(object.getPassword())) {
                validationResult.add(Error.of(400, "В пароле отсутствуют буквы"));
            }
            if (object.getPassword().toLowerCase().equals(object.getPassword())) {
                validationResult.add(Error.of(400, "В пароле отсутствуют заглавные буквы"));
            }
            if (object.getPassword().toUpperCase().equals(object.getPassword())) {
                validationResult.add(Error.of(400, "В пароле отсутствуют прописные буквы"));
            }
            if (object.getPassword().replaceAll("\\d", "").equals(object.getPassword())) {
                validationResult.add(Error.of(400, "В пароле отсутствуют цифры"));
            }
            if (object.getPassword().replaceAll("[a-zA-Z\\d]", "").length() != 0) {
                validationResult.add(Error.of(400, "В пароле отсутствуют спец символы"));
            }
            if (object.getPassword().equals(object.getLogin())) {
                validationResult.add(Error.of(400, "Пароль не может совпадать с логином"));
            }
        }

        return validationResult;
    }
}
