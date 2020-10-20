package dulikkk.livehealthierapi.domain.statistics.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class TrainingStatisticsDto {

    int allTrainings;

    int doneAllTrainings;

    int allTrainingsThisMonth;

    int doneTrainingsThisMonth;

    int allTrainingsThisWeek;

    int doneTrainingsThisWeek;

}
