package dulikkk.livehealthierapi.domain.user;

import dulikkk.livehealthierapi.domain.plan.PlanDomainFacade;
import dulikkk.livehealthierapi.domain.statistics.StatisticsDomainFacade;
import dulikkk.livehealthierapi.domain.user.dto.command.NewUserCommand;
import dulikkk.livehealthierapi.domain.user.dto.command.UpdateHeightCommand;
import dulikkk.livehealthierapi.domain.user.dto.command.UpdateWeightCommand;
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
                            TokenSender tokenSender, ActivationTokenCreator activationTokenCreator,
                            PlanDomainFacade planDomainFacade, StatisticsDomainFacade statisticsDomainFacade) {
        UserValidator userValidator = new UserValidator(userQueryRepository);
        BMICalculator bmiCalculator = new BMICalculator();
        this.userActivator = new UserActivator(tokenSender, activationTokenCreator, userRepository, userQueryRepository);
        this.userCreator = new UserCreator(userRepository, encoder, userActivator, userValidator,
                planDomainFacade, statisticsDomainFacade, bmiCalculator);
        this.userInfoUpdater = new UserInfoUpdater(userValidator, userQueryRepository, userRepository,
                statisticsDomainFacade, planDomainFacade, bmiCalculator);
    }

    public String addNewUser(NewUserCommand newUserCommand) {
        return userCreator.createUser(newUserCommand);
    }

    public void activateUser(String token) {
        userActivator.validateTokenAndActivateUser(token);
    }

    public void updateHeight(UpdateHeightCommand updateHeightCommand) {
        userInfoUpdater.updateHeight(updateHeightCommand);
    }

    public void updateWeight(UpdateWeightCommand updateWeightCommand) {
        userInfoUpdater.updateWeight(updateWeightCommand);
    }
}
