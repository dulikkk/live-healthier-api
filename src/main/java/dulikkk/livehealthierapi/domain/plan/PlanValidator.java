package dulikkk.livehealthierapi.domain.plan;

import dulikkk.livehealthierapi.domain.plan.dto.command.ChangeTrainingCommand;
import dulikkk.livehealthierapi.domain.plan.dto.exception.PlanException;
import org.apache.commons.lang3.StringUtils;

class PlanValidator {

    void validateChangeTrainingCommand(ChangeTrainingCommand changeTrainingCommand){
        if(changeTrainingCommand.getDayOfWeek() == null){
            throw new PlanException("Nie podano dnia tygodnia. Spróbuj ponownie później");
        }

        if(StringUtils.isBlank(changeTrainingCommand.getUserId())){
            throw new PlanException("Nie podano identyfikatora użytkownika. Spróbuj ponownie później");
        }

        if(changeTrainingCommand.getDifficultyLevelDto() == null){
            throw new PlanException("Nie podano poziomu trudności treningu. Spróbuj ponownie później");
        }
    }
}
