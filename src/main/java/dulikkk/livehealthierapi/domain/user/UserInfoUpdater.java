package dulikkk.livehealthierapi.domain.user;

import dulikkk.livehealthierapi.domain.user.dto.*;
import dulikkk.livehealthierapi.domain.user.dto.exception.CannotFindUserException;
import dulikkk.livehealthierapi.domain.user.port.outgoing.UserRepository;
import dulikkk.livehealthierapi.domain.user.query.UserQueryRepository;
import lombok.RequiredArgsConstructor;

import static java.time.LocalDate.now;

@RequiredArgsConstructor
class UserInfoUpdater {

    private final UserValidator userValidator;
    private final UserCreator userCreator;
    private final UserQueryRepository userQueryRepository;
    private final UserRepository userRepository;

    void updateUserInfo(String userId, NewUserInfoCommand newUserInfoCommand) {
        userValidator.validateUserInfo(newUserInfoCommand);

        UserDto userDto = userQueryRepository.findById(userId)
                .orElseThrow(() -> new CannotFindUserException("Nie ma takiego użytkownika"));

        UserInfoDto newUserInfoDto = updateNewUserInfoDto(userDto, newUserInfoCommand);

                UserDto userToSaveWithId = UserDto.builder()
                .id(userId)
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .roles(userDto.getRoles())
                .active(userDto.isActive())
                .userInfoDto(newUserInfoDto)
                .build();

        userRepository.updateUser(userToSaveWithId);
    }

    private UserInfoDto updateNewUserInfoDto(UserDto userDto, NewUserInfoCommand newUserInfoCommand) {
        UserHeightDto updatedUserHeightDto = updateUserHeightDto(newUserInfoCommand.getHeightInCm(),
                userDto.getUserInfoDto().getUserHeightDto());

        UserWeightDto updatedUserWeightDto = updateUserWeightDto(newUserInfoCommand.getWeightInKg(),
                userDto.getUserInfoDto().getUserWeightDto());

        return UserInfoDto.builder()
                .sex(newUserInfoCommand.getSex())
                .birthday(newUserInfoCommand.getBirthdate())
                .userHeightDto(updatedUserHeightDto)
                .userWeightDto(updatedUserWeightDto)
                .bmi(userCreator.calculateBMI(newUserInfoCommand.getWeightInKg(),
                        newUserInfoCommand.getHeightInCm()))
                .build();
    }

    private UserHeightDto updateUserHeightDto(double newHeightInCm, UserHeightDto oldUserHeightDto) {
        return UserHeightDto.builder()
                .currentHeightInCm(newHeightInCm)
                .lastHeightInCm(oldUserHeightDto.getLastHeightInCm())
                .initialHeightInCm(oldUserHeightDto.getInitialHeightInCm())
                .lastUpdateDate(now())
                .initialDate(oldUserHeightDto.getInitialDate())
                .build();
    }

    private UserWeightDto updateUserWeightDto(double newWeightInKg, UserWeightDto userWeightDto) {
        return UserWeightDto.builder()
                .currentWeightInKg(newWeightInKg)
                .lastWeightInKg(userWeightDto.getLastWeightInKg())
                .initialWeightInKg(userWeightDto.getInitialWeightInKg())
                .lastUpdateDate(now())
                .initialDate(userWeightDto.getInitialDate())
                .build();
    }
}
