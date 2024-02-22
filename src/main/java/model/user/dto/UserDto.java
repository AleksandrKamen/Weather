package model.user.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserDto {
    private String login;
    private String password;
    private String confirmPassword;
}
