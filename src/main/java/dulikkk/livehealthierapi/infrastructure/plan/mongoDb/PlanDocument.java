package dulikkk.livehealthierapi.infrastructure.plan.mongoDb;

import dulikkk.livehealthierapi.domain.plan.dto.DifficultyLevelDto;
import dulikkk.livehealthierapi.domain.plan.dto.TrainingDto;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Value
@Document
public class PlanDocument {

    @Id
    String id;

    String userId;

    DifficultyLevelDto userLevel;

    TrainingDto monday;

    TrainingDto tuesday;

    TrainingDto wednesday;

    TrainingDto thursday;

    TrainingDto friday;

    TrainingDto saturday;

    TrainingDto sunday;
}
