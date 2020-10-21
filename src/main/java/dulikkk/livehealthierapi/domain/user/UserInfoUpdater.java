package dulikkk.livehealthierapi.domain.user;

import dulikkk.livehealthierapi.domain.plan.PlanDomainFacade;
import dulikkk.livehealthierapi.domain.plan.dto.command.NewPlanCommand;
import dulikkk.livehealthierapi.domain.statistics.StatisticsDomainFacade;
import dulikkk.livehealthierapi.domain.statistics.dto.command.UpdateBmiStatisticsCommand;
import dulikkk.livehealthierapi.domain.statistics.dto.command.UpdateHeightStatisticsCommand;
import dulikkk.livehealthierapi.domain.statistics.dto.command.UpdateWeightStatisticsCommand;
import dulikkk.livehealthierapi.domain.user.dto.*;
import dulikkk.livehealthierapi.domain.user.dto.command.UpdateHeightCommand;
import dulikkk.livehealthierapi.domain.user.dto.command.UpdateWeightCommand;
import dulikkk.livehealthierapi.domain.user.dto.exception.CannotFindUserException;
import dulikkk.livehealthierapi.domain.user.port.outgoing.UserRepository;
import dulikkk.livehealthierapi.domain.user.query.UserQueryRepository;
import lombok.RequiredArgsConstructor;

import java.time.Year;

@RequiredArgsConstructor
class UserInfoUpdater {

    private final UserValidator userValidator;
    private final UserQueryRepository userQueryRepository;
    private final UserRepository userRepository;
    private final StatisticsDomainFacade statisticsDomainFacade;
    private final PlanDomainFacade planDomainFacade;
    private final BMICalculator bmiCalculator;

    public void updateHeight(UpdateHeightCommand updateHeightCommand) {
        userValidator.validateHeightInCm(updateHeightCommand.getNewHeightInCm());

        UserDto userDto = userQueryRepository.findById(updateHeightCommand.getUserId())
                .orElseThrow(() -> new CannotFindUserException("Nie znaleziono użytkownika o podanym identyfikatorze"));

        double newBmi = bmiCalculator.calculateBMI(userDto.getUserInfoDto().getWeightInKg(),
                updateHeightCommand.getNewHeightInCm());

        UserInfoDto updatedUserInfo = UserInfoDto.builder()
                .birthday(userDto.getUserInfoDto().getBirthday())
                .bmi(newBmi)
                .heightInCm(updateHeightCommand.getNewHeightInCm())
                .weightInKg(userDto.getUserInfoDto().getWeightInKg())
                .sex(userDto.getUserInfoDto().getSex())
                .build();

        UserDto updatedUser = UserDto.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .active(userDto.isActive())
                .password(userDto.getPassword())
                .roles(userDto.getRoles())
                .userInfoDto(updatedUserInfo)
                .build();
        userRepository.updateUser(updatedUser);

        UpdateHeightStatisticsCommand updateHeightStatisticsCommand = new UpdateHeightStatisticsCommand(
                updateHeightCommand.getUserId(), updatedUserInfo.getHeightInCm());
        statisticsDomainFacade.updateHeightStatistics(updateHeightStatisticsCommand);

        UpdateBmiStatisticsCommand updateBmiStatisticsCommand = new UpdateBmiStatisticsCommand(
                updateHeightCommand.getUserId(), newBmi);
        statisticsDomainFacade.updateBmiStatistics(updateBmiStatisticsCommand);

        updatePlan(userDto.getUserInfoDto().getBirthday(), updateHeightCommand.getUserId(), newBmi);

    }

    public void updateWeight(UpdateWeightCommand updateWeightCommand) {
        userValidator.validateWeightInKg(updateWeightCommand.getNewWeightInKg());

        UserDto userDto = userQueryRepository.findById(updateWeightCommand.getUserId())
                .orElseThrow(() -> new CannotFindUserException("Nie znaleziono użytkownika o podanym identyfikatorze"));

        double newBmi = bmiCalculator.calculateBMI(updateWeightCommand.getNewWeightInKg(),
                userDto.getUserInfoDto().getHeightInCm());

        UserInfoDto updatedUserInfo = UserInfoDto.builder()
                .birthday(userDto.getUserInfoDto().getBirthday())
                .bmi(newBmi)
                .heightInCm(userDto.getUserInfoDto().getHeightInCm())
                .weightInKg(updateWeightCommand.getNewWeightInKg())
                .sex(userDto.getUserInfoDto().getSex())
                .build();

        UserDto updatedUser = UserDto.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .active(userDto.isActive())
                .password(userDto.getPassword())
                .roles(userDto.getRoles())
                .userInfoDto(updatedUserInfo)
                .build();
        userRepository.updateUser(updatedUser);

        UpdateWeightStatisticsCommand updateWeightStatisticsCommand = new UpdateWeightStatisticsCommand(
                updateWeightCommand.getUserId(), updatedUserInfo.getWeightInKg());
        statisticsDomainFacade.updateWeightStatistics(updateWeightStatisticsCommand);

        UpdateBmiStatisticsCommand updateBmiStatisticsCommand = new UpdateBmiStatisticsCommand(
                updateWeightCommand.getUserId(), newBmi);
        statisticsDomainFacade.updateBmiStatistics(updateBmiStatisticsCommand);

        updatePlan(userDto.getUserInfoDto().getBirthday(), updateWeightCommand.getUserId(), newBmi);
    }

    private void updatePlan(int birthday, String userId, double newBmi) {
        int userAge = Year.now().getValue() - birthday;
        planDomainFacade.updatePlanByNewBmiOrAge(userId, newBmi, userAge);
    }

}
