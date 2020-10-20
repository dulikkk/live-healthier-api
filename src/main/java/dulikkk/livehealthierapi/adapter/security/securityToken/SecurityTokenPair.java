package dulikkk.livehealthierapi.adapter.security.securityToken;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
@Getter
public class SecurityTokenPair {

    String accessToken;

    String refreshToken;
}
