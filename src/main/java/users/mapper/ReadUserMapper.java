package users.mapper;

import users.dto.UserDto;
import users.entity.UsersEntity;
import util.mapper.Mapper;

public class ReadUserMapper implements Mapper<UsersEntity, UserDto> {
    @Override
    public UserDto mapFrom(UsersEntity object) {
        return UserDto.builder()
                .login(object.getLogin())
                .password(object.getPassword())
                .build();
    }
}
