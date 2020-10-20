package dulikkk.livehealthierapi.domain.statistics.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Builder
@Value
public class WeightStatisticsDto {

    double initialWeightInKg;

    double lastWeightInKg;

    LocalDate initialDate;

    LocalDate lastUpdateDate;
}
