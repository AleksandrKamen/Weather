package model.users.service;

import model.users.dto.UserDto;
import model.users.entity.UsersEntity;
import model.users.mapper.CreateUserMapper;
import model.users.repository.UserRepository;
import validator.exception.ValidationException;
import validator.user.CreateUserValidator;

public class UserService {
    private static final UserRepository userRepository = new UserRepository();
    private static final CreateUserMapper creatUserMapper = new CreateUserMapper();
    private static final CreateUserValidator createUserValidator = new CreateUserValidator();

     public UsersEntity createUser(UserDto userDto){
         var validationResult = createUserValidator.isValid(userDto);
         if (!validationResult.isValid()){
             throw  new ValidationException(validationResult.getErrors());
         }
         var usersEntity = creatUserMapper.mapFrom(userDto);
         var save = userRepository.save(usersEntity);
         return save;
     }

}
