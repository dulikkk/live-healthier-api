package dulikkk.livehealthierapi.infrastructure.user.mongoDb;

import dulikkk.livehealthierapi.domain.user.dto.UserDto;
import dulikkk.livehealthierapi.domain.user.port.outgoing.UserRepository;
import dulikkk.livehealthierapi.infrastructure.mongoDb.util.MongoDbQueryAndUpdateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
class MongoDbUserRepository implements UserRepository {

    private final MongoTemplate mongoTemplate;
    private final UserConverter userConverter = new UserConverter();
    private final MongoDbQueryAndUpdateUtil mongoDbQueryAndUpdateUtil = new MongoDbQueryAndUpdateUtil();

    @Override
    public UserDto saveUser(UserDto userDto) {
        UserDocument savedUser = mongoTemplate.insert(userConverter.toDocument(userDto));

        return userConverter.toDto(savedUser);
    }

    @Override
    public void activateUser(UserDto activatedUser) {
        mongoTemplate.updateFirst(mongoDbQueryAndUpdateUtil.userIdQuery(activatedUser.getId()),
                mongoDbQueryAndUpdateUtil.activateUserUpdate(), UserDocument.class);
        mongoTemplate.updateFirst(mongoDbQueryAndUpdateUtil.userIdQuery(activatedUser.getId()),
                mongoDbQueryAndUpdateUtil.deleteActivationTokenUpdate(), UserDocument.class);
    }


    @Override
    public void updateUser(UserDto userDto) {
        mongoTemplate.updateFirst(mongoDbQueryAndUpdateUtil.userIdQuery(userDto.getId()),
                mongoDbQueryAndUpdateUtil.updateUser(userConverter.toDocument(userDto)), UserDocument.class);
    }

    @Override
    public void saveActivationToken(String token, String userId) {
        mongoTemplate.updateFirst(mongoDbQueryAndUpdateUtil.userIdQuery(userId),
                mongoDbQueryAndUpdateUtil.setActivationTokenUpdate(token), UserDocument.class);
    }

    @Override
    public boolean isTokenExists(String token) {
        return mongoTemplate.exists(mongoDbQueryAndUpdateUtil.activationTokenQuery(token), UserDocument.class);
    }

    @Override
    public void restoreActivationToken(String newToken, String userId) {
        mongoTemplate.updateFirst(mongoDbQueryAndUpdateUtil.userIdQuery(userId),
                mongoDbQueryAndUpdateUtil.setActivationTokenUpdate(newToken), UserDocument.class);
    }
}
