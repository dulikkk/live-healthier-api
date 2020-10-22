package dulikkk.livehealthierapi.domain.plan.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PlanDto {

    String id;

    String userId;

    DifficultyLevelDto userLevel;

    int availableChangesThiWeek;

    TrainingDto monday;

    TrainingDto tuesday;

    TrainingDto wednesday;

    TrainingDto thursday;

    TrainingDto friday;

    TrainingDto saturday;

    TrainingDto sunday;
}
