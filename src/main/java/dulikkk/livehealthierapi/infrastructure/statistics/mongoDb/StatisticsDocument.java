package dulikkk.livehealthierapi.infrastructure.statistics.mongoDb;

import dulikkk.livehealthierapi.domain.statistics.dto.*;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@Document
public class StatisticsDocument {

    private String id;

    private final String userId;

    private final HeightStatisticsDto heightStatisticsDto;

    private final WeightStatisticsDto weightStatisticsDto;

    private final SuperChallengeStatisticsDto superChallengeStatisticsDto;

    private final TrainingStatisticsDto trainingStatisticsDto;

    private final BmiStatisticsDto bmiStatisticsDto;
}
