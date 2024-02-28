package model.user.mapper;

import model.user.dto.UserDto;
import model.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.mindrot.jbcrypt.BCrypt;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "password", target = "password", qualifiedByName = "hashPassword")
    User userDtoToUser(UserDto userDto);

    @Named("hashPassword")
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
