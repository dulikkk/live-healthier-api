package dulikkk.livehealthierapi.domain.statistics;

import dulikkk.livehealthierapi.domain.statistics.port.outgoing.StatisticsRepository;
import dulikkk.livehealthierapi.domain.statistics.query.StatisticsQueryRepository;
import dulikkk.livehealthierapi.domain.user.query.UserQueryRepository;
import dulikkk.livehealthierapi.infrastructure.statistics.memory.InMemoryStatisticsRepository;
import dulikkk.livehealthierapi.infrastructure.user.memory.InMemoryUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StatisticsDomainConfigurator {

    public StatisticsDomainFacade statisticsDomainFacade(InMemoryStatisticsRepository inMemoryStatisticsRepository,
                                                         InMemoryUserRepository inMemoryUserRepository) {
        return new StatisticsDomainFacade(inMemoryStatisticsRepository, inMemoryStatisticsRepository, inMemoryUserRepository);
    }

    @Bean
    public StatisticsDomainFacade statisticsDomainFacade(StatisticsRepository statisticsRepository,
                                                         StatisticsQueryRepository statisticsQueryRepository,
                                                         UserQueryRepository userQueryRepository) {
        return new StatisticsDomainFacade(statisticsRepository, statisticsQueryRepository, userQueryRepository);
    }
}
