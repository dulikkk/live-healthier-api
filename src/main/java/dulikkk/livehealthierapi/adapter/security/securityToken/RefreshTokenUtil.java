package dulikkk.livehealthierapi.adapter.security.securityToken;

import dulikkk.livehealthierapi.adapter.security.AuthException;
import dulikkk.livehealthierapi.domain.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.UUID.randomUUID;

@RequiredArgsConstructor
@Component
public class RefreshTokenUtil {

    private final RefreshTokenRepository refreshTokenRepository;
    private final AccessTokenUtil accessTokenUtil = new AccessTokenUtil();

    public String generateRefreshToken(String username) {
        String refreshToken = randomUUID().toString();
        refreshTokenRepository.saveRefreshToken(refreshToken, username);

        return refreshToken;
    }

    public SecurityTokenPair refreshAccessAndRefreshToken(String refreshToken) {
        UserDto userFromRefreshToken = refreshTokenRepository.getUserByRefreshToken(refreshToken)
                .orElseThrow(() -> new AuthException("Nie ma takiego tokena odświeżającego. Spróbuj ponownie później"));

        deleteRefreshToken(refreshToken);

        List<String> roles = userFromRefreshToken.getRoles()
                .stream()
                .map(Enum::toString)
                .collect(Collectors.toUnmodifiableList());

        String newAccessToken = accessTokenUtil.generateAccessToken(userFromRefreshToken.getId(), roles);
        String newRefreshToken = generateRefreshToken(userFromRefreshToken.getUsername());

        return new SecurityTokenPair(newAccessToken, newRefreshToken);
    }

    public void deleteRefreshToken(String refreshToken) {
        refreshTokenRepository.getUserByRefreshToken(refreshToken)
                .orElseThrow(() -> new AuthException("Nie ma takiego tokena odświeżającego. Spróbuj ponownie później"));
        refreshTokenRepository.deleteRefreshToken(refreshToken);
    }

}
