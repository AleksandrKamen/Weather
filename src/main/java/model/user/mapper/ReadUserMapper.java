package model.user.mapper;

import model.user.dto.UserDto;
import model.user.entity.User;
import util.mapper.Mapper;

public class ReadUserMapper implements Mapper<User, UserDto> {
    @Override
    public UserDto mapFrom(User object) {
        return UserDto.builder()
                .login(object.getLogin())
                .build();
    }
}
