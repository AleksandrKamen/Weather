package users.mapper;

import org.mindrot.jbcrypt.BCrypt;
import users.dto.UserDto;
import users.entity.UsersEntity;
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
