package dulikkk.livehealthierapi.domain.user;

import dulikkk.livehealthierapi.domain.user.dto.NewUserCommand;
import dulikkk.livehealthierapi.domain.user.dto.NewUserInfoCommand;
import dulikkk.livehealthierapi.domain.user.port.outgoing.ActivationTokenCreator;
import dulikkk.livehealthierapi.domain.user.port.outgoing.Encoder;
import dulikkk.livehealthierapi.domain.user.port.outgoing.TokenSender;
import dulikkk.livehealthierapi.domain.user.port.outgoing.UserRepository;
import dulikkk.livehealthierapi.domain.user.query.UserQueryRepository;

public class UserDomainFacade {

    private final UserActivator userActivator;
    private final UserCreator userCreator;
    private final UserInfoUpdater userInfoUpdater;

    public UserDomainFacade(UserRepository userRepository, UserQueryRepository userQueryRepository, Encoder encoder,
                            TokenSender tokenSender, ActivationTokenCreator activationTokenCreator) {
        UserValidator userValidator = new UserValidator(userQueryRepository);

        this.userActivator = new UserActivator(tokenSender, activationTokenCreator, userRepository, userQueryRepository);
        this.userCreator = new UserCreator(userRepository, encoder, userActivator, userValidator);
        this.userInfoUpdater = new UserInfoUpdater(userValidator, userCreator, userQueryRepository, userRepository);
    }

    public String addNewUser(NewUserCommand newUserCommand) {
        return userCreator.createUser(newUserCommand);
    }

    public void activateUser(String token) {
        userActivator.validateTokenAndActivateUser(token);
    }

    public void updateUserInfo(String userId, NewUserInfoCommand newUserInfoCommand) {
        userInfoUpdater.updateUserInfo(userId, newUserInfoCommand);
    }
}
