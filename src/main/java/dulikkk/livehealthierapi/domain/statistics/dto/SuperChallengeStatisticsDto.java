package dulikkk.livehealthierapi.domain.statistics.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class SuperChallengeStatisticsDto {

    int allSuperChallenges;

    int doneAllSuperChallenges;

    int allSuperChallengesThisMonth;

    int doneSuperChallengesThisMonth;

    int allSuperChallengesThisWeek;

    int doneSuperChallengesThisWeek;
}
