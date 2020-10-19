package dulikkk.livehealthierapi.domain.plan.port.outgoing;

import dulikkk.livehealthierapi.domain.plan.dto.DifficultyLevelDto;
import dulikkk.livehealthierapi.domain.plan.dto.PlanDto;
import dulikkk.livehealthierapi.domain.plan.dto.TrainingDto;

public interface PlanRepository {

    TrainingDto getRandomTrainingByDifficultyLevel(DifficultyLevelDto difficultyLevelDto);

    TrainingDto getBreakDay();

    void savePlan(PlanDto planDto);

    void updateUserPlan(PlanDto planDto);
}
