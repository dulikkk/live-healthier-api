package dulikkk.livehealthierapi.domain.plan.query;

import dulikkk.livehealthierapi.domain.plan.dto.PlanDto;

public interface PlanQueryRepository {

    PlanDto getPlanByUserId(String userId);
}
