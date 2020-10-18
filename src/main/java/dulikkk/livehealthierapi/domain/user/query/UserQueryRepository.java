package dulikkk.livehealthierapi.domain.user.query;

import dulikkk.livehealthierapi.domain.user.dto.UserDto;

import java.util.Optional;

public interface UserQueryRepository {

    Optional<UserDto> findByUsername(String username);

    Optional<UserDto> findByEmail(String email);

    Optional<UserDto> findById(String id);

    UserDto getUserFromActivationToken(String token);
}
