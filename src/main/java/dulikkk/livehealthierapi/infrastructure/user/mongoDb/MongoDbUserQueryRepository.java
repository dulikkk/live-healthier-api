package dulikkk.livehealthierapi.infrastructure.user.mongoDb;

import dulikkk.livehealthierapi.domain.user.dto.UserDto;
import dulikkk.livehealthierapi.domain.user.query.UserQueryRepository;
import dulikkk.livehealthierapi.infrastructure.mongoDb.util.MongoDbQueryAndUpdateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
class MongoDbUserQueryRepository implements UserQueryRepository {

    private final MongoTemplate mongoTemplate;
    private final UserConverter userConverter = new UserConverter();
    private final MongoDbQueryAndUpdateUtil mongoDbQueryAndUpdateUtil = new MongoDbQueryAndUpdateUtil();

    @Override
    public Optional<UserDto> findByUsername(String username) {
        return Optional.ofNullable(mongoTemplate.findOne(mongoDbQueryAndUpdateUtil.usernameQuery(username),
                UserDocument.class))
                .map(userConverter::toDto);
    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
        return Optional.ofNullable(mongoTemplate.findOne(mongoDbQueryAndUpdateUtil.emailQuery(email),
                UserDocument.class))
                .map(userConverter::toDto);
    }

    @Override
    public Optional<UserDto> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, UserDocument.class))
                .map(userConverter::toDto);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public UserDto getUserFromActivationToken(String token) {
        UserDocument userDocument = mongoTemplate.findOne(mongoDbQueryAndUpdateUtil.activationTokenQuery(token),
                UserDocument.class);

        return userConverter.toDto(userDocument);
    }
}
