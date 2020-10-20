package dulikkk.livehealthierapi.domain.statistics.dto.command;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class InitialStatisticsCommand {

    String userId;

    double heightInCm;

    double weightInKg;

    double bmi;
}
