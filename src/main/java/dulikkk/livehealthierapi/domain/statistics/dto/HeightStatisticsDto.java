package dulikkk.livehealthierapi.domain.statistics.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Builder
@Value
public class HeightStatisticsDto {

    double initialHeightInCm;

    double lastHeightInCm;

    LocalDate initialDate;

    LocalDate lastUpdateDate;
}
