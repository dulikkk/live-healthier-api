package dulikkk.livehealthierapi.adapter.security.securityToken;

import dulikkk.livehealthierapi.domain.user.dto.UserDto;

import java.util.Optional;

public interface RefreshTokenRepository {

    void saveRefreshToken(String refreshToken, String username);

    void deleteRefreshToken(String refreshToken);

    Optional<UserDto> getUserByRefreshToken(String refreshToken);
}
