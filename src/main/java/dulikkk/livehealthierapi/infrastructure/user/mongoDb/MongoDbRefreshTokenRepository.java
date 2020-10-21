package dulikkk.livehealthierapi.infrastructure.user.mongoDb;

import dulikkk.livehealthierapi.adapter.security.securityToken.RefreshTokenRepository;
import dulikkk.livehealthierapi.domain.user.dto.UserDto;
import dulikkk.livehealthierapi.infrastructure.mongoDb.util.MongoDbQueryAndUpdateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
class MongoDbRefreshTokenRepository implements RefreshTokenRepository {

    private final MongoTemplate mongoTemplate;
    private final UserConverter userConverter = new UserConverter();
    private final MongoDbQueryAndUpdateUtil mongoDbQueryAndUpdateUtil = new MongoDbQueryAndUpdateUtil();

    @Override
    public void saveRefreshToken(String refreshToken, String username) {
        mongoTemplate.updateFirst(mongoDbQueryAndUpdateUtil.usernameQuery(username),
                mongoDbQueryAndUpdateUtil.setRefreshTokenUpdate(refreshToken),
                UserDocument.class);
    }

    @Override
    public void deleteRefreshToken(String refreshToken) {
        mongoTemplate.updateFirst(mongoDbQueryAndUpdateUtil.refreshTokenQuery(refreshToken),
                mongoDbQueryAndUpdateUtil.deleteRefreshTokenUpdate(),
                UserDocument.class);
    }

    @Override
    public Optional<UserDto> getUserByRefreshToken(String refreshToken) {
        return Optional.ofNullable(mongoTemplate.findOne(mongoDbQueryAndUpdateUtil.refreshTokenQuery(refreshToken),
                UserDocument.class))
                .map(userConverter::toDto);
    }
}

