package dulikkk.livehealthierapi.domain.user.port.outgoing;

import java.time.LocalDateTime;

public interface ActivationTokenCreator {

    String generateToken();

    LocalDateTime getExpirationDateFromToken(String token);
}
