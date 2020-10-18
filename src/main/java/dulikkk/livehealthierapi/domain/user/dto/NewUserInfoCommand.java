package dulikkk.livehealthierapi.domain.user.dto;

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
