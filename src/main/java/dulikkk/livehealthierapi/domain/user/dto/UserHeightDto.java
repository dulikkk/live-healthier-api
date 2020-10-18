package dulikkk.livehealthierapi.domain.user.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Builder
@Value
public class UserHeightDto {

    double initialHeightInCm;

    double lastHeightInCm;

    double currentHeightInCm;

    LocalDate initialDate;

    LocalDate lastUpdateDate;
}
