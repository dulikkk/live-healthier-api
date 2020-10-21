package dulikkk.livehealthierapi.infrastructure.statistics.memory;

import dulikkk.livehealthierapi.domain.statistics.dto.StatisticsDto;
import dulikkk.livehealthierapi.domain.statistics.port.outgoing.StatisticsRepository;
import dulikkk.livehealthierapi.domain.statistics.query.StatisticsQueryRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.UUID.randomUUID;

public class InMemoryStatisticsRepository implements StatisticsRepository, StatisticsQueryRepository {

    private final Map<String, StatisticsDto> statisticsRepo = new ConcurrentHashMap<>();

    @Override
    public void saveStatistics(StatisticsDto statisticsDto) {
        StatisticsDto statisticsDtoToSave = StatisticsDto.builder()
                .heightStatisticsDto(statisticsDto.getHeightStatisticsDto())
                .weightStatisticsDto(statisticsDto.getWeightStatisticsDto())
                .bmiStatisticsDto(statisticsDto.getBmiStatisticsDto())
                .superChallengeStatisticsDto(statisticsDto.getSuperChallengeStatisticsDto())
                .trainingStatisticsDto(statisticsDto.getTrainingStatisticsDto())
                .userId(statisticsDto.getUserId())
                .id(String.valueOf(randomUUID()))
                .build();

        statisticsRepo.put(statisticsDtoToSave.getId(), statisticsDtoToSave);
    }

    @Override
    public void updateStatistics(StatisticsDto statisticsDto) {
            statisticsRepo.replace(statisticsDto.getId(), statisticsDto);
    }

    @Override
    public List<StatisticsDto> getAllStatistics() {
        return null;
    }

    @Override
    public Optional<StatisticsDto> getStatisticsByUserId(String userId) {
        return statisticsRepo.values()
                .stream()
                .filter(statisticsDto -> statisticsDto.getUserId().equals(userId))
                .findAny();
    }
}
