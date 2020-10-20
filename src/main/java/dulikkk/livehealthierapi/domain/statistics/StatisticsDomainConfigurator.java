package dulikkk.livehealthierapi.domain.statistics;

import dulikkk.livehealthierapi.infrastructure.statistics.memory.InMemoryStatisticsRepository;
import dulikkk.livehealthierapi.infrastructure.user.memory.InMemoryUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StatisticsDomainConfigurator {

    @Bean
    public StatisticsDomainFacade statisticsDomainFacade(InMemoryStatisticsRepository inMemoryStatisticsRepository,
                                                         InMemoryUserRepository inMemoryUserRepository) {
        return new StatisticsDomainFacade(inMemoryStatisticsRepository, inMemoryStatisticsRepository, inMemoryUserRepository);
    }
}
