package dulikkk.livehealthierapi.domain.user.dto.command;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
@Value
public class UpdateWeightCommand {
    String userId;

    double newWeightInKg;

}
