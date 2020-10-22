package dulikkk.livehealthierapi.adapter.incoming.api.statistics;

import dulikkk.livehealthierapi.domain.statistics.StatisticsDomainFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StatisticsSchedulingUpdate {

    private final StatisticsDomainFacade statisticsDomainFacade;

    @Scheduled(cron = "0 0 0 * * *")
    void updateStatisticsOnNewDay() {
        statisticsDomainFacade.updateStatisticsOnNewDay();
    }
}
