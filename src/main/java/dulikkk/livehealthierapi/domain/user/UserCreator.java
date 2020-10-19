package dulikkk.livehealthierapi.domain.user;

import dulikkk.livehealthierapi.domain.plan.PlanDomainFacade;
import dulikkk.livehealthierapi.domain.plan.dto.NewPlanCommand;
import dulikkk.livehealthierapi.domain.user.dto.*;
import dulikkk.livehealthierapi.domain.user.port.outgoing.Encoder;
import dulikkk.livehealthierapi.domain.user.port.outgoing.UserRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Year;
import java.util.Set;

import static java.time.LocalDate.now;

@RequiredArgsConstructor
class UserCreator {

    private final UserRepository userRepository;
    private final Encoder encoder;
    private final UserActivator userActivator;
    private final UserValidator userValidator;
    private final PlanDomainFacade planDomainFacade;

    String createUser(NewUserCommand newUserCommand) {
        userValidator.validateNewUser(newUserCommand);

        UserDto newUserDtoToSave = encodePasswordAndCreateNewUserDto(newUserCommand);

        UserDto savedUser = userRepository.saveUser(newUserDtoToSave);

        new Thread(() -> userActivator.createAndSendActivationToken(savedUser.getId(), savedUser.getEmail())).start();

        createPlanForNewUser(savedUser);

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
        UserWeightDto userWeightDto = UserWeightDto.builder()
                .currentWeightInKg(newUserInfoCommand.getWeightInKg())
                .initialWeightInKg(newUserInfoCommand.getWeightInKg())
                .lastWeightInKg(newUserInfoCommand.getWeightInKg())
                .initialDate(now())
                .lastUpdateDate(now())
                .build();

        UserHeightDto userHeightDto = UserHeightDto.builder()
                .currentHeightInCm(newUserInfoCommand.getHeightInCm())
                .initialHeightInCm(newUserInfoCommand.getHeightInCm())
                .lastHeightInCm(newUserInfoCommand.getHeightInCm())
                .initialDate(now())
                .lastUpdateDate(now())
                .build();

        return UserInfoDto.builder()
                .sex(newUserInfoCommand.getSex())
                .birthday(newUserInfoCommand.getBirthdate())
                .userHeightDto(userHeightDto)
                .userWeightDto(userWeightDto)
                .bmi(calculateBMI(newUserInfoCommand.getWeightInKg(),
                        newUserInfoCommand.getHeightInCm()))
                .build();
    }

    private void createPlanForNewUser(UserDto newUser) {
        int userAge = Year.now().getValue() - newUser.getUserInfoDto().getBirthday();
        NewPlanCommand newPlanCommand = new NewPlanCommand(newUser.getId(), newUser.getUserInfoDto().getBmi(), userAge);
        planDomainFacade.createPlanForUser(newPlanCommand);
    }
}
