package dulikkk.livehealthierapi.domain.plan.dto.command;


import dulikkk.livehealthierapi.domain.plan.dto.DifficultyLevelDto;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.time.DayOfWeek;

@RequiredArgsConstructor
@Value
public class ChangeTrainingCommand {

    String userId;

    DayOfWeek dayOfWeek;

    DifficultyLevelDto difficultyLevelDto;

}
