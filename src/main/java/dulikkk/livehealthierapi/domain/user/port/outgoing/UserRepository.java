package dulikkk.livehealthierapi.domain.user.port.outgoing;

import dulikkk.livehealthierapi.domain.user.dto.UserDto;

public interface UserRepository {

    UserDto createUser(UserDto userDto);

    void updateUser(UserDto userDto);

    void activateUser(UserDto userToUpdate);

    void saveActivationToken(String token, String userId);

    boolean isTokenExists(String token);

    void restoreActivationToken(String newToken, String userId);
}
