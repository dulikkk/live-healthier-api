package dulikkk.livehealthierapi.domain.statistics.port.outgoing;

import dulikkk.livehealthierapi.domain.statistics.dto.StatisticsDto;

import java.util.List;

public interface StatisticsRepository {

    void saveStatistics(StatisticsDto statisticsDto);

    void updateStatistics(StatisticsDto statisticsDto);

    List<StatisticsDto> getAllStatistics();
}