package dulikkk.livehealthierapi.infrastructure.user.memory;

import dulikkk.livehealthierapi.domain.user.dto.UserDto;
import dulikkk.livehealthierapi.domain.user.dto.exception.CannotFindUserException;
import dulikkk.livehealthierapi.domain.user.port.outgoing.UserRepository;
import dulikkk.livehealthierapi.domain.user.query.UserQueryRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.UUID.randomUUID;

public class InMemoryUserRepository implements UserRepository, UserQueryRepository {

    private  Map<String, UserDto> usersRepo = new ConcurrentHashMap<>();

    // UserId, Token
    private  Map<String, String> tokensRepo = new ConcurrentHashMap<>();

    @Override
    public UserDto createUser(UserDto userDto) {
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

}

