package model.user.mapper;

import model.user.dto.UserDto;
import model.user.entity.UserEntity;
import util.mapper.Mapper;

public class ReadUserMapper implements Mapper<UserEntity, UserDto> {
    @Override
    public UserDto mapFrom(UserEntity object) {
        return UserDto.builder()
                .login(object.getLogin())
                .build();
    }
}
