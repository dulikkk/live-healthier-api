package dulikkk.livehealthierapi.domain.statistics.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class StatisticsDto {

    String id;

    String userId;

    HeightStatisticsDto heightStatisticsDto;

    WeightStatisticsDto weightStatisticsDto;

    SuperChallengeStatisticsDto superChallengeStatisticsDto;

    TrainingStatisticsDto trainingStatisticsDto;

    BmiStatisticsDto bmiStatisticsDto;
}
