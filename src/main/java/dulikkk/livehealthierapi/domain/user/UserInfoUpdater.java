package dulikkk.livehealthierapi.domain.user;

import dulikkk.livehealthierapi.domain.statistics.StatisticsDomainFacade;
import dulikkk.livehealthierapi.domain.statistics.dto.command.UpdateBmiStatisticsCommand;
import dulikkk.livehealthierapi.domain.statistics.dto.command.UpdateHeightStatisticsCommand;
import dulikkk.livehealthierapi.domain.statistics.dto.command.UpdateWeightStatisticsCommand;
import dulikkk.livehealthierapi.domain.user.dto.*;
import dulikkk.livehealthierapi.domain.user.dto.command.UpdateBmiCommand;
import dulikkk.livehealthierapi.domain.user.dto.command.UpdateHeightCommand;
import dulikkk.livehealthierapi.domain.user.dto.command.UpdateWeightCommand;
import dulikkk.livehealthierapi.domain.user.dto.exception.CannotFindUserException;
import dulikkk.livehealthierapi.domain.user.port.outgoing.UserRepository;
import dulikkk.livehealthierapi.domain.user.query.UserQueryRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class UserInfoUpdater {

    private final UserValidator userValidator;
    private final UserQueryRepository userQueryRepository;
    private final UserRepository userRepository;
    private final StatisticsDomainFacade statisticsDomainFacade;

    public void updateBmi(UpdateBmiCommand updateBmiCommand) {
        userValidator.validateBmi(updateBmiCommand.getNewBmi());

        UserDto userDto = userQueryRepository.findById(updateBmiCommand.getUserId())
                .orElseThrow(() -> new CannotFindUserException("Nie znaleziono użytkownika o podanym identyfikatorze"));

        UserInfoDto updatedUserInfo = UserInfoDto.builder()
                .birthday(userDto.getUserInfoDto().getBirthday())
                .bmi(updateBmiCommand.getNewBmi())
                .heightInCm(userDto.getUserInfoDto().getHeightInCm())
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

        UpdateBmiStatisticsCommand updateBmiStatisticsCommand = new UpdateBmiStatisticsCommand(
                updateBmiCommand.getUserId(), userDto.getUserInfoDto().getBmi());
        statisticsDomainFacade.updateBmiStatistics(updateBmiStatisticsCommand);
    }

    public void updateHeight(UpdateHeightCommand updateHeightCommand) {
        userValidator.validateHeightInCm(updateHeightCommand.getNewHeightInCm());

        UserDto userDto = userQueryRepository.findById(updateHeightCommand.getUserId())
                .orElseThrow(() -> new CannotFindUserException("Nie znaleziono użytkownika o podanym identyfikatorze"));

        UserInfoDto updatedUserInfo = UserInfoDto.builder()
                .birthday(userDto.getUserInfoDto().getBirthday())
                .bmi(userDto.getUserInfoDto().getBmi())
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
                updateHeightCommand.getUserId(), userDto.getUserInfoDto().getBmi());
        statisticsDomainFacade.updateHeightStatistics(updateHeightStatisticsCommand);
    }

    public void updateWeight(UpdateWeightCommand updateWeightCommand) {
        userValidator.validateWeightInKg(updateWeightCommand.getNewWeightInKg());

        UserDto userDto = userQueryRepository.findById(updateWeightCommand.getUserId())
                .orElseThrow(() -> new CannotFindUserException("Nie znaleziono użytkownika o podanym identyfikatorze"));

        UserInfoDto updatedUserInfo = UserInfoDto.builder()
                .birthday(userDto.getUserInfoDto().getBirthday())
                .bmi(userDto.getUserInfoDto().getBmi())
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
                updateWeightCommand.getUserId(), userDto.getUserInfoDto().getBmi());
        statisticsDomainFacade.updateWeightStatistics(updateWeightStatisticsCommand);
    }


}
