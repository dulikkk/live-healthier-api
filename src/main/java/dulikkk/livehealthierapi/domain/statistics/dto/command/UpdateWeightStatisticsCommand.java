package dulikkk.livehealthierapi.domain.statistics.dto.command;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
@Value
public class UpdateWeightStatisticsCommand {

    String userId;

    double lastCurrentWeightInKg;
}
