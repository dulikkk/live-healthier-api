package dulikkk.livehealthierapi.domain.plan.dto;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
@Value
public class NewPlanCommand {

    String userId;

    double bmi;

    int age;
}
