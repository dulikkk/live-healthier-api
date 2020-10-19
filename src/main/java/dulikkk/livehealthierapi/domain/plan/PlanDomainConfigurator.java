package dulikkk.livehealthierapi.domain.plan;

import dulikkk.livehealthierapi.infrastructure.plan.memory.InMemoryPlanRepository;
import dulikkk.livehealthierapi.infrastructure.user.memory.InMemoryUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PlanDomainConfigurator {

    @Bean
    public PlanDomainFacade planDomainFacade(InMemoryPlanRepository inMemoryPlanRepository, InMemoryUserRepository inMemoryUserRepository) {
        return new PlanDomainFacade(inMemoryPlanRepository, inMemoryPlanRepository, inMemoryUserRepository);
    }
}
