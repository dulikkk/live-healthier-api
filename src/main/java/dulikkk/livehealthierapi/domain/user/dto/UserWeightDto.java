package dulikkk.livehealthierapi.domain.user.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Builder
@Value
public class UserWeightDto {

    double initialWeightInKg;

    double lastWeightInKg;

    double currentWeightInKg;

    LocalDate initialDate;

    LocalDate lastUpdateDate;
}
