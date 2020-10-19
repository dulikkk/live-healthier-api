package dulikkk.livehealthierapi.domain.plan.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TrainingDto {

    String id;

    TrainingTypeDto trainingTypeDto;

    DifficultyLevelDto trainingDifficultyDto;

    String description;
}
