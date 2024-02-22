package model.user.mapper;

import model.user.dto.UserDto;
import org.mindrot.jbcrypt.BCrypt;
import model.user.entity.UserEntity;
import util.mapper.Mapper;

public class CreateUserMapper implements Mapper<UserDto, UserEntity> {
    @Override
    public UserEntity mapFrom(UserDto object) {
            return UserEntity.builder()
                    .login(object.getLogin())
                    .password(BCrypt.hashpw(object.getPassword(),BCrypt.gensalt()))
                    .build();
    }
}
