package dulikkk.livehealthierapi.adapter.security.authentication;

import dulikkk.livehealthierapi.domain.user.dto.UserRoleDto;
import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Builder
@Value
public class AuthenticatedUserInfo {

    String id;

    String username;

    String email;

    Set<UserRoleDto> roles;

    double heightInCm;

    double weightInKg;

    double bmi;
}
