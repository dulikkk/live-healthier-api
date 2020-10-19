package dulikkk.livehealthierapi.domain.plan;

import dulikkk.livehealthierapi.domain.user.query.UserQueryRepository;
import dulikkk.livehealthierapi.infrastructure.plan.memory.InMemoryPlanRepository;
import dulikkk.livehealthierapi.infrastructure.user.memory.InMemoryUserRepository;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlanDomainConfigurator {

    public PlanDomainFacade planDomainFacade(InMemoryPlanRepository inMemoryPlanRepository, InMemoryUserRepository inMemoryUserRepository) {
        return new PlanDomainFacade(inMemoryPlanRepository, inMemoryPlanRepository, inMemoryUserRepository);
    }
}
