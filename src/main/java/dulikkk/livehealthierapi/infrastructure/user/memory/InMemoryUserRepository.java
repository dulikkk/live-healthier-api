package dulikkk.livehealthierapi.infrastructure.user.memory;

import dulikkk.livehealthierapi.adapter.security.securityToken.RefreshTokenRepository;
import dulikkk.livehealthierapi.domain.user.dto.UserDto;
import dulikkk.livehealthierapi.domain.user.dto.exception.CannotFindUserException;
import dulikkk.livehealthierapi.domain.user.port.outgoing.UserRepository;
import dulikkk.livehealthierapi.domain.user.query.UserQueryRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.UUID.randomUUID;

public class InMemoryUserRepository implements UserRepository, UserQueryRepository, RefreshTokenRepository {

    private final Map<String, UserDto> usersRepo = new ConcurrentHashMap<>();

    // UserId, Token
    private final Map<String, String> tokensRepo = new ConcurrentHashMap<>();

    // refresh token, username
    private final Map<String, String> refreshTokens = new ConcurrentHashMap<>();

    @Override
    public UserDto saveUser(UserDto userDto) {
        UserDto userToSave = UserDto.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .roles(userDto.getRoles())
                .active(userDto.isActive())
                .userInfoDto(userDto.getUserInfoDto())
                .id(randomUUID().toString())
                .build();
        usersRepo.put(userToSave.getId(), userToSave);
        return userToSave;
    }

    @Override
    public void updateUser(UserDto userDto) {
        usersRepo.replace(userDto.getId(), userDto);
    }

    @Override
    public Optional<UserDto> findByUsername(String username) {
        return usersRepo.values()
                .stream()
                .filter(user -> user.getUsername().equals(username))
                .findAny();
    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
        return usersRepo.values()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .findAny();
    }

    @Override
    public Optional<UserDto> findById(String userId) {
        return usersRepo.values()
                .stream()
                .filter(user -> user.getId().equals(userId))
                .findAny();
    }

    @Override
    public UserDto getUserFromActivationToken(String token) {
        String userId = tokensRepo.entrySet()
                .stream()
                .filter(tokenRepo -> token.equals(tokenRepo.getValue()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new CannotFindUserException("Nie można znaleźć takiego tokenu takiego użytkownika :("));

        return usersRepo.get(userId);
    }

    @Override
    public void activateUser(UserDto userToUpdate) {
        usersRepo.replace(userToUpdate.getId(), userToUpdate);

        tokensRepo.remove(userToUpdate.getId());
    }

    @Override
    public void saveActivationToken(String token, String userId) {
        if (usersRepo.containsKey(userId)) {
            tokensRepo.put(userId, token);
        }
    }

    @Override
    public boolean isTokenExists(String token) {
        return tokensRepo.values()
                .stream()
                .anyMatch(token::equals);
    }

    @Override
    public void restoreActivationToken(String newToken, String userId) {
        tokensRepo.replace(userId, newToken);
    }


    @Override
    public void saveRefreshToken(String refreshToken, String username) {
        refreshTokens.put(refreshToken, username);
    }

    @Override
    public void deleteRefreshToken(String refreshToken) {
        refreshTokens.remove(refreshToken);
    }

    @Override
    public Optional<UserDto> getUserByRefreshToken(String refreshToken) {
        return usersRepo.values()
                .stream()
                .filter(userDto -> userDto.getUsername().equals(refreshTokens.get(refreshToken)))
                .findAny();
    }
}

