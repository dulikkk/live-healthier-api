package dulikkk.livehealthierapi.domain.user;

import dulikkk.livehealthierapi.domain.user.port.outgoing.ActivationTokenCreator;
import dulikkk.livehealthierapi.domain.user.port.outgoing.Encoder;
import dulikkk.livehealthierapi.domain.user.port.outgoing.TokenSender;
import dulikkk.livehealthierapi.infrastructure.user.memory.InMemoryUserRepository;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserDomainConfigurator {

    public UserDomainFacade userDomainFacade(InMemoryUserRepository inMemoryUserRepository, Encoder encoder,
                                             ActivationTokenCreator activationTokenCreator, TokenSender tokenSender) {
        return new UserDomainFacade(inMemoryUserRepository, inMemoryUserRepository, encoder, tokenSender,
                activationTokenCreator);
    }
}
