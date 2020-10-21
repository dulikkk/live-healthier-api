package dulikkk.livehealthierapi.infrastructure.statistics.mongoDb;

import dulikkk.livehealthierapi.domain.statistics.dto.StatisticsDto;
import dulikkk.livehealthierapi.domain.statistics.port.outgoing.StatisticsRepository;
import dulikkk.livehealthierapi.infrastructure.mongoDb.util.MongoDbQueryAndUpdateUtil;
import dulikkk.livehealthierapi.infrastructure.user.mongoDb.UserDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
class MongoDbStatisticsRepository implements StatisticsRepository {

    private final MongoTemplate mongoTemplate;
    private final StatisticsConverter statisticsConverter = new StatisticsConverter();
    private final MongoDbQueryAndUpdateUtil mongoDbQueryAndUpdateUtil = new MongoDbQueryAndUpdateUtil();

    @Override
    public void saveStatistics(StatisticsDto statisticsDto) {
        mongoTemplate.updateFirst(mongoDbQueryAndUpdateUtil.userIdQuery(statisticsDto.getUserId()),
                mongoDbQueryAndUpdateUtil.addStatistics(statisticsConverter.toDocument(statisticsDto)), UserDocument.class);
    }

    @Override
    public void updateStatistics(StatisticsDto statisticsDto) {
        saveStatistics(statisticsDto);
    }

    @Override
    public List<StatisticsDto> getAllStatistics() {
        return mongoTemplate.find(new Query(), StatisticsDocument.class, "user")
                .stream()
                .map(statisticsConverter::toDto)
                .collect(Collectors.toList());
    }
}
