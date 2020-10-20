package dulikkk.livehealthierapi.domain.statistics.query;

import dulikkk.livehealthierapi.domain.statistics.dto.StatisticsDto;

import java.util.Optional;

public interface StatisticsQueryRepository {

    Optional<StatisticsDto> getStatisticsByUserId(String userId);
}
