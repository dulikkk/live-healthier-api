package dulikkk.livehealthierapi.domain.user;

import dulikkk.livehealthierapi.domain.user.dto.UserDto;
import dulikkk.livehealthierapi.domain.user.dto.exception.UserException;
import dulikkk.livehealthierapi.domain.user.port.outgoing.ActivationTokenCreator;
import dulikkk.livehealthierapi.domain.user.port.outgoing.TokenSender;
import dulikkk.livehealthierapi.domain.user.port.outgoing.UserRepository;
import dulikkk.livehealthierapi.domain.user.query.UserQueryRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
class UserActivator {

    private final TokenSender tokenSender;
    private final ActivationTokenCreator activationTokenCreator;
    private final UserRepository userRepository;
    private final UserQueryRepository userQueryRepository;

    void createAndSendActivationToken(String userId, String receiver) {
        String activationToken = activationTokenCreator.generateToken();
        userRepository.saveActivationToken(activationToken, userId);
        tokenSender.sendToken(activationToken, receiver);
    }

    void validateTokenAndActivateUser(String token) {

        if (!userRepository.isTokenExists(token)) {
            throw new UserException("Taki token aktywacyjny nie istnieje :( Skontaktuj się z nami w celu rozwiązania problemu");
        }

        UserDto userFromActivationToken = userQueryRepository.getUserFromActivationToken(token);

        LocalDateTime tokenExpirationDate = activationTokenCreator.getExpirationDateFromToken(token);
        if (!isGoodDate(tokenExpirationDate)) {
            resendToken(userFromActivationToken);
            throw new UserException("Ten link aktywacyjny jest przestarzały. Wysłaliśmy do ciebie nowy!");
        } else {
            activateUser(userFromActivationToken);
        }
    }


    private void activateUser(UserDto userToActivate) {
        UserDto activatedUser = UserDto.builder()
                .id(userToActivate.getId())
                .username(userToActivate.getUsername())
                .active(true)
                .email(userToActivate.getEmail())
                .roles(userToActivate.getRoles())
                .password(userToActivate.getPassword())
                .build();

        userRepository.activateUser(activatedUser);
    }

    private boolean isGoodDate(LocalDateTime expirationDate) {
        LocalDateTime now = LocalDateTime.now();
        return now.isBefore(expirationDate);
    }

    private void resendToken(UserDto receiver) {
        String newActivationToken = activationTokenCreator.generateToken();
        userRepository.restoreActivationToken(newActivationToken, receiver.getId());
        tokenSender.sendToken(newActivationToken, receiver.getEmail());
    }
}
