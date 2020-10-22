package dulikkk.livehealthierapi.infrastructure.plan.mongoDb;

import dulikkk.livehealthierapi.domain.plan.dto.PlanDto;
import dulikkk.livehealthierapi.domain.plan.query.PlanQueryRepository;
import dulikkk.livehealthierapi.infrastructure.mongoDb.util.MongoDbQueryAndUpdateUtil;
import dulikkk.livehealthierapi.infrastructure.user.mongoDb.UserDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
class MongoDbPlanQueryRepository implements PlanQueryRepository {

    private final MongoTemplate mongoTemplate;
    private final PlanConverter planConverter = new PlanConverter();
    private final MongoDbQueryAndUpdateUtil mongoDbQueryAndUpdateUtil = new MongoDbQueryAndUpdateUtil();

    @Override
    public Optional<PlanDto> getPlanByUserId(String userId) {
        return Optional.ofNullable(mongoTemplate
                .findOne(mongoDbQueryAndUpdateUtil.userIdQuery(userId), UserDocument.class, "user"))
                .map(UserDocument::getPlan)
                .map(planConverter::toDto);
    }
}
