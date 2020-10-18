package dulikkk.livehealthierapi.domain.user.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class UserInfoDto {

    SexDto sex;

    int birthday;

    UserWeightDto userWeightDto;

    UserHeightDto userHeightDto;

    double bmi;
}
