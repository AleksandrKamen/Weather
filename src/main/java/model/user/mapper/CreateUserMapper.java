package model.user.mapper;

import model.user.dto.UserDto;
import org.mindrot.jbcrypt.BCrypt;
import model.user.entity.User;
import util.mapper.Mapper;

public class CreateUserMapper implements Mapper<UserDto, User> {
    @Override
    public User mapFrom(UserDto object) {
            return User.builder()
                    .login(object.getLogin())
                    .password(BCrypt.hashpw(object.getPassword(),BCrypt.gensalt()))
                    .build();
    }
}
