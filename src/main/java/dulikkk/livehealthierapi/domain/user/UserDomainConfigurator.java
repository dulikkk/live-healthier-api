package dulikkk.livehealthierapi.domain.user;

import dulikkk.livehealthierapi.domain.plan.PlanDomainFacade;
import dulikkk.livehealthierapi.domain.user.port.outgoing.ActivationTokenCreator;
import dulikkk.livehealthierapi.domain.user.port.outgoing.Encoder;
import dulikkk.livehealthierapi.domain.user.port.outgoing.TokenSender;
import dulikkk.livehealthierapi.infrastructure.user.memory.InMemoryUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserDomainConfigurator {

    @Bean
    public UserDomainFacade userDomainFacade(InMemoryUserRepository inMemoryUserRepository, Encoder encoder,
                                             ActivationTokenCreator activationTokenCreator, TokenSender tokenSender,
                                             PlanDomainFacade planDomainFacade) {
        return new UserDomainFacade(inMemoryUserRepository, inMemoryUserRepository, encoder, tokenSender,
                activationTokenCreator, planDomainFacade);
    }
}
