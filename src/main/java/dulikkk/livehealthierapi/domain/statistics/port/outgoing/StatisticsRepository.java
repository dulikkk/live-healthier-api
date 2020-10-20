package dulikkk.livehealthierapi.domain.statistics.port.outgoing;

import dulikkk.livehealthierapi.domain.statistics.dto.StatisticsDto;

public interface StatisticsRepository {

    void saveStatistics(StatisticsDto statisticsDto);

    void updateStatistics(StatisticsDto statisticsDto);

    void incrementDaysOfTrainingAndSuperChallenge();

    void clearMonthStatistics();

    void clearWeeksStatistics();
}
