package dulikkk.livehealthierapi.infrastructure.statistics.mongoDb;

import dulikkk.livehealthierapi.domain.statistics.dto.StatisticsDto;
import dulikkk.livehealthierapi.domain.statistics.query.StatisticsQueryRepository;
import dulikkk.livehealthierapi.infrastructure.mongoDb.util.MongoDbQueryAndUpdateUtil;
import dulikkk.livehealthierapi.infrastructure.user.mongoDb.UserDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
class MongoDbStatisticsQueryRepository implements StatisticsQueryRepository {

    private final MongoTemplate mongoTemplate;
    private final StatisticsConverter statisticsConverter = new StatisticsConverter();
    private final MongoDbQueryAndUpdateUtil mongoDbQueryAndUpdateUtil = new MongoDbQueryAndUpdateUtil();

    @Override
    public Optional<StatisticsDto> getStatisticsByUserId(String userId) {
        return Optional.ofNullable(mongoTemplate
                .findOne(mongoDbQueryAndUpdateUtil.statisticsQueryByUserId(userId), UserDocument.class, "user"))
                .map(UserDocument::getStatistics)
                .map(statisticsConverter::toDto);
    }
}
