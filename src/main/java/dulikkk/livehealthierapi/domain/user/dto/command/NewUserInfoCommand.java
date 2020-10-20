package dulikkk.livehealthierapi.domain.user.dto.command;

import dulikkk.livehealthierapi.domain.user.dto.SexDto;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class NewUserInfoCommand {

    SexDto sex;

    int birthdate;

    double heightInCm;

    double weightInKg;
}
