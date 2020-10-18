package dulikkk.livehealthierapi.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.util.Set;

@Builder
@Value
public class UserDto {

    String id;

    String username;

    String email;

    String password;

    Set<UserRoleDto> roles;

    boolean active;

    UserInfoDto userInfoDto;
}
