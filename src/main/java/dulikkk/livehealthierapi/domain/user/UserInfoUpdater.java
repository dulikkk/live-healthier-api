package dulikkk.livehealthierapi.domain.user;

import dulikkk.livehealthierapi.domain.user.dto.*;
import dulikkk.livehealthierapi.domain.user.dto.exception.CannotFindUserException;
import dulikkk.livehealthierapi.domain.user.port.outgoing.UserRepository;
import dulikkk.livehealthierapi.domain.user.query.UserQueryRepository;
import lombok.RequiredArgsConstructor;

import static java.time.LocalDate.now;
import static java.util.UUID.randomUUID;

@RequiredArgsConstructor
class UserInfoUpdater {

    private final UserValidator userValidator;
    private final UserCreator userCreator;
    private final UserQueryRepository userQueryRepository;
    private final UserRepository userRepository;

    void updateUserInfo(String userId, NewUserInfoCommand newUserInfoCommand){
        userValidator.validateUserInfo(newUserInfoCommand);

        UserDto userDto = userQueryRepository.findById(userId)
                .orElseThrow(() -> new CannotFindUserException("Nie ma takiego użytkownika"));

        UserHeightDto currentUserHeightDto = userDto.getUserInfoDto().getUserHeightDto();
        UserWeightDto currentUserWeightDto = userDto.getUserInfoDto().getUserWeightDto();

        UserHeightDto newUserHeightDto = UserHeightDto.builder()
                .currentHeightInCm(newUserInfoCommand.getHeightInCm())
                .lastHeightInCm(currentUserHeightDto.getLastHeightInCm())
                .initialHeightInCm(currentUserHeightDto.getInitialHeightInCm())
                .lastUpdateDate(now())
                .initialDate(currentUserHeightDto.getInitialDate())
                .build();

        UserWeightDto newUserWeightDto = UserWeightDto.builder()
                .currentWeightInKg(newUserInfoCommand.getWeightInKg())
                .lastWeightInKg(currentUserWeightDto.getLastWeightInKg())
                .initialWeightInKg(currentUserWeightDto.getInitialWeightInKg())
                .lastUpdateDate(now())
                .initialDate(currentUserHeightDto.getInitialDate())
                .build();

        UserInfoDto newUserInfoDto = UserInfoDto.builder()
                .sex(newUserInfoCommand.getSex())
                .birthday(newUserInfoCommand.getBirthdate())
                .userHeightDto(newUserHeightDto)
                .userWeightDto(newUserWeightDto)
                .bmi(userCreator.calculateBMI(newUserInfoCommand.getWeightInKg(),
                        newUserInfoCommand.getHeightInCm()))
                .build();

        UserDto userToSave = UserDto.builder()
                .id(userId)
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .roles(userDto.getRoles())
                .active(userDto.isActive())
                .userInfoDto(newUserInfoDto)
                .build();

        userRepository.updateUser(userToSave);
    }
}
