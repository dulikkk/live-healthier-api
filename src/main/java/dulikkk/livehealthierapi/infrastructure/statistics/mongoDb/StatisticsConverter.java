package dulikkk.livehealthierapi.infrastructure.statistics.mongoDb;

import dulikkk.livehealthierapi.domain.statistics.dto.StatisticsDto;

public class StatisticsConverter {

    public StatisticsDocument toDocument(StatisticsDto statisticsDto){
        return StatisticsDocument.builder()
                .id(statisticsDto.getId())
                .userId(statisticsDto.getUserId())
                .bmiStatisticsDto(statisticsDto.getBmiStatisticsDto())
                .heightStatisticsDto(statisticsDto.getHeightStatisticsDto())
                .weightStatisticsDto(statisticsDto.getWeightStatisticsDto())
                .superChallengeStatisticsDto(statisticsDto.getSuperChallengeStatisticsDto())
                .trainingStatisticsDto(statisticsDto.getTrainingStatisticsDto())
                .build();
    }

    public StatisticsDto toDto(StatisticsDocument statisticsDocument){
        return StatisticsDto.builder()
                .id(statisticsDocument.getId())
                .userId(statisticsDocument.getUserId())
                .bmiStatisticsDto(statisticsDocument.getBmiStatisticsDto())
                .heightStatisticsDto(statisticsDocument.getHeightStatisticsDto())
                .weightStatisticsDto(statisticsDocument.getWeightStatisticsDto())
                .superChallengeStatisticsDto(statisticsDocument.getSuperChallengeStatisticsDto())
                .trainingStatisticsDto(statisticsDocument.getTrainingStatisticsDto())
                .build();
    }
}
