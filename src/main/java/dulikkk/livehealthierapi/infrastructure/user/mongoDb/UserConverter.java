package dulikkk.livehealthierapi.infrastructure.user.mongoDb;

import dulikkk.livehealthierapi.domain.user.dto.UserDto;

class UserConverter {

    UserDocument toDocument(UserDto userDto){
        return UserDocument.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .active(userDto.isActive())
                .roles(userDto.getRoles())
                .info(userDto.getUserInfoDto())
                .build();
    }

    UserDto toDto(UserDocument userDocument){
        return UserDto.builder()
                .id(userDocument.getId())
                .username(userDocument.getUsername())
                .email(userDocument.getEmail())
                .password(userDocument.getPassword())
                .active(userDocument.isActive())
                .roles(userDocument.getRoles())
                .userInfoDto(userDocument.getInfo())
                .build();
    }
}
