package dulikkk.livehealthierapi.infrastructure.plan.mongoDb.training;

import dulikkk.livehealthierapi.domain.plan.dto.DifficultyLevelDto;
import dulikkk.livehealthierapi.domain.plan.dto.TrainingTypeDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@Document("trainings")
public class TrainingDocument {

    @Id
    private String id;

    private final TrainingTypeDto trainingType;

    private final DifficultyLevelDto trainingDifficulty;

    private final String description;

}
