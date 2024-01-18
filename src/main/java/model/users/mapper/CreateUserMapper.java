package model.users.mapper;

import model.users.dto.UserDto;
import org.mindrot.jbcrypt.BCrypt;
import model.users.entity.UsersEntity;
import util.mapper.Mapper;

public class CreateUserMapper implements Mapper<UserDto, UsersEntity> {
    @Override
    public UsersEntity mapFrom(UserDto object) {
            return UsersEntity.builder()
                    .login(object.getLogin())
                    .password(BCrypt.hashpw(object.getPassword(),BCrypt.gensalt()))
                    .build();
    }
}
