package dulikkk.livehealthierapi.adapter.incoming.api.plan;

import dulikkk.livehealthierapi.domain.plan.dto.TrainingDto;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class PlanResponse {

    int availableChangesThiWeek;

    TrainingDto monday;

    TrainingDto tuesday;

    TrainingDto wednesday;

    TrainingDto thursday;

    TrainingDto friday;

    TrainingDto saturday;

    TrainingDto sunday;

    TrainingDto today;
}
