package dulikkk.livehealthierapi.domain.statistics.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Builder
@Value
public class BmiStatisticsDto {

    double initialBmi;

    double lastBmi;

    LocalDate initialDate;

    LocalDate lastUpdateDate;
}
