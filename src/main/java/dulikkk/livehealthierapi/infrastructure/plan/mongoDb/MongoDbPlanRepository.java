package dulikkk.livehealthierapi.infrastructure.plan.mongoDb;

import dulikkk.livehealthierapi.domain.plan.dto.DifficultyLevelDto;
import dulikkk.livehealthierapi.domain.plan.dto.PlanDto;
import dulikkk.livehealthierapi.domain.plan.dto.TrainingDto;
import dulikkk.livehealthierapi.domain.plan.dto.exception.PlanServerException;
import dulikkk.livehealthierapi.domain.plan.port.outgoing.PlanRepository;
import dulikkk.livehealthierapi.infrastructure.mongoDb.util.MongoDbQueryAndUpdateUtil;
import dulikkk.livehealthierapi.infrastructure.plan.mongoDb.training.TrainingConverter;
import dulikkk.livehealthierapi.infrastructure.plan.mongoDb.training.TrainingDocument;
import dulikkk.livehealthierapi.infrastructure.user.mongoDb.UserDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Repository
class MongoDbPlanRepository implements PlanRepository {

    private final MongoTemplate mongoTemplate;
    private final TrainingConverter trainingConverter = new TrainingConverter();
    private final PlanConverter planConverter = new PlanConverter();
    private final MongoDbQueryAndUpdateUtil mongoDbQueryAndUpdateUtil = new MongoDbQueryAndUpdateUtil();

    @Override
    public TrainingDto getRandomTrainingByDifficultyLevel(DifficultyLevelDto difficultyLevelDto) {
        List<TrainingDocument> trainings = new ArrayList<>(mongoTemplate
                .find(mongoDbQueryAndUpdateUtil.trainingDifficultyLevel(difficultyLevelDto), TrainingDocument.class));
        Random random = new Random();
        if (!trainings.isEmpty()) {
            return trainingConverter.toDto(trainings.get(random.nextInt(trainings.size())));
        } else {
            throw new PlanServerException("Nie znaleziono treningu :( Spróbuj ponownie później");
        }
    }

    @Override
    public TrainingDto getBreakDay() {
        return mongoTemplate.find(mongoDbQueryAndUpdateUtil.trainingBreakDay(), TrainingDocument.class)
                .stream()
                .findAny()
                .map(trainingConverter::toDto)
                .orElseThrow(() -> new PlanServerException("Nie znaleziono treningu :( Spróbuj ponownie później"));
    }

    @Override
    public void savePlan(PlanDto planDto) {
        mongoTemplate.updateFirst(mongoDbQueryAndUpdateUtil.userIdQuery(planDto.getUserId()),
                mongoDbQueryAndUpdateUtil.addPlan(planConverter.toDocument(planDto)), UserDocument.class);
    }

    @Override
    public void updateUserPlan(PlanDto planDto) {
        savePlan(planDto);
    }
}
