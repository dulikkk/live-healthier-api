package dulikkk.livehealthierapi.infrastructure.statistics.mongoDb;

import dulikkk.livehealthierapi.domain.statistics.dto.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
@Value
@Document
public class StatisticsDocument {

    String id;

    String userId;

    HeightStatisticsDto heightStatistics;

    WeightStatisticsDto weightStatistics;

    SuperChallengeStatisticsDto superChallengeStatistics;

    TrainingStatisticsDto trainingStatistics;

    BmiStatisticsDto bmiStatistics;

    boolean todayTrainingDone;

    boolean todaySuperChallengeDone;
}
