package dulikkk.livehealthierapi.domain.plan.query;

import dulikkk.livehealthierapi.domain.plan.dto.PlanDto;

import java.util.Optional;

public interface PlanQueryRepository {

    Optional<PlanDto> getPlanByUserId(String userId);
}
