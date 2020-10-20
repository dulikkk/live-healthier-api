package dulikkk.livehealthierapi.domain.user;

import dulikkk.livehealthierapi.domain.plan.PlanDomainFacade;
import dulikkk.livehealthierapi.domain.plan.dto.command.NewPlanCommand;
import dulikkk.livehealthierapi.domain.statistics.StatisticsDomainFacade;
import dulikkk.livehealthierapi.domain.statistics.dto.command.InitialStatisticsCommand;
import dulikkk.livehealthierapi.domain.user.dto.*;
import dulikkk.livehealthierapi.domain.user.dto.command.NewUserCommand;
import dulikkk.livehealthierapi.domain.user.dto.command.NewUserInfoCommand;
import dulikkk.livehealthierapi.domain.user.port.outgoing.Encoder;
import dulikkk.livehealthierapi.domain.user.port.outgoing.UserRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Year;
import java.util.Set;

@RequiredArgsConstructor
class UserCreator {

    private final UserRepository userRepository;
    private final Encoder encoder;
    private final UserActivator userActivator;
    private final UserValidator userValidator;
    private final PlanDomainFacade planDomainFacade;
    private final StatisticsDomainFacade statisticsDomainFacade;

    String createUser(NewUserCommand newUserCommand) {
        userValidator.validateNewUser(newUserCommand);

        UserDto newUserDtoToSave = encodePasswordAndCreateNewUserDto(newUserCommand);

        UserDto savedUser = userRepository.saveUser(newUserDtoToSave);

        new Thread(() -> userActivator.createAndSendActivationToken(savedUser.getId(), savedUser.getEmail())).start();

        createPlanForNewUser(savedUser);
        initialStatistics(savedUser.getId(), newUserCommand.getNewUserInfoCommand());

        return savedUser.getId();
    }

    public double calculateBMI(double weightInKg, double heightInCm) {
        BigDecimal weightBigDecimal = BigDecimal.valueOf(weightInKg);
        BigDecimal heightInMBigDecimal = BigDecimal.valueOf(heightInCm / 100);

        // weightInKg / (heightInM * heightInM)
        return weightBigDecimal.divide(heightInMBigDecimal.multiply(heightInMBigDecimal), new MathContext(4))
                .doubleValue();
    }

    private UserDto encodePasswordAndCreateNewUserDto(NewUserCommand newUserCommand) {
        String encodedPassword = encoder.encode(newUserCommand.getPassword());

        return UserDto.builder()
                .username(newUserCommand.getUsername())
                .email(newUserCommand.getEmail())
                .password(encodedPassword)
                .roles(Set.of(UserRoleDto.USER))
                .active(false)
                .userInfoDto(createUserInfoDto(newUserCommand.getNewUserInfoCommand()))
                .build();
    }

    private UserInfoDto createUserInfoDto(NewUserInfoCommand newUserInfoCommand) {
        return UserInfoDto.builder()
                .sex(newUserInfoCommand.getSex())
                .birthday(newUserInfoCommand.getBirthdate())
                .heightInCm(newUserInfoCommand.getHeightInCm())
                .weightInKg(newUserInfoCommand.getWeightInKg())
                .bmi(calculateBMI(newUserInfoCommand.getWeightInKg(),
                        newUserInfoCommand.getHeightInCm()))
                .build();
    }

    private void initialStatistics(String userId, NewUserInfoCommand newUserInfoCommand) {
        InitialStatisticsCommand initialStatisticsCommand = InitialStatisticsCommand.builder()
                .heightInCm(newUserInfoCommand.getHeightInCm())
                .weightInKg(newUserInfoCommand.getWeightInKg())
                .userId(userId)
                .build();
        statisticsDomainFacade.initialStatistics(initialStatisticsCommand);
    }

    private void createPlanForNewUser(UserDto newUser) {
        int userAge = Year.now().getValue() - newUser.getUserInfoDto().getBirthday();
        NewPlanCommand newPlanCommand = new NewPlanCommand(newUser.getId(), newUser.getUserInfoDto().getBmi(), userAge);
        planDomainFacade.createPlanForUser(newPlanCommand);
    }
}
