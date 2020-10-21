package dulikkk.livehealthierapi.infrastructure.plan.mongoDb;

import dulikkk.livehealthierapi.domain.plan.dto.DifficultyLevelDto;
import dulikkk.livehealthierapi.domain.plan.dto.TrainingDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@Document()
public class PlanDocument {

    @Id
    private String id;

    private final String userId;

    private final DifficultyLevelDto userLevel;

    private final TrainingDto monday;

    private final TrainingDto tuesday;

    private final TrainingDto wednesday;

    private final TrainingDto thursday;

    private final TrainingDto friday;

    private final TrainingDto saturday;

    private final TrainingDto sunday;
}
