package dulikkk.livehealthierapi.infrastructure.statistics.mongoDb;

import dulikkk.livehealthierapi.domain.statistics.dto.StatisticsDto;

public class StatisticsConverter {

    public StatisticsDocument toDocument(StatisticsDto statisticsDto){
        return StatisticsDocument.builder()
                .id(statisticsDto.getId())
                .userId(statisticsDto.getUserId())
                .bmiStatistics(statisticsDto.getBmiStatisticsDto())
                .heightStatistics(statisticsDto.getHeightStatisticsDto())
                .weightStatistics(statisticsDto.getWeightStatisticsDto())
                .superChallengeStatistics(statisticsDto.getSuperChallengeStatisticsDto())
                .trainingStatistics(statisticsDto.getTrainingStatisticsDto())
                .build();
    }

    public StatisticsDto toDto(StatisticsDocument statisticsDocument){
        return StatisticsDto.builder()
                .id(statisticsDocument.getId())
                .userId(statisticsDocument.getUserId())
                .bmiStatisticsDto(statisticsDocument.getBmiStatistics())
                .heightStatisticsDto(statisticsDocument.getHeightStatistics())
                .weightStatisticsDto(statisticsDocument.getWeightStatistics())
                .superChallengeStatisticsDto(statisticsDocument.getSuperChallengeStatistics())
                .trainingStatisticsDto(statisticsDocument.getTrainingStatistics())
                .build();
    }
}
