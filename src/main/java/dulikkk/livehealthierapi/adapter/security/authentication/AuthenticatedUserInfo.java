package dulikkk.livehealthierapi.adapter.security.authentication;

import dulikkk.livehealthierapi.domain.user.dto.UserRoleDto;
import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Builder
@Value
class AuthenticatedUserInfo {

    String username;

    String email;

    Set<UserRoleDto> roles;
}
