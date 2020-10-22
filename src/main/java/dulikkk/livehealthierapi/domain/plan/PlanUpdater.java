package dulikkk.livehealthierapi.domain.plan;

import dulikkk.livehealthierapi.domain.plan.dto.DifficultyLevelDto;
import dulikkk.livehealthierapi.domain.plan.dto.PlanDto;
import dulikkk.livehealthierapi.domain.plan.dto.TrainingDto;
import dulikkk.livehealthierapi.domain.plan.dto.command.ChangeTrainingCommand;
import dulikkk.livehealthierapi.domain.plan.dto.exception.CannotFindPlanException;
import dulikkk.livehealthierapi.domain.plan.dto.exception.PlanException;
import dulikkk.livehealthierapi.domain.plan.port.outgoing.PlanRepository;
import dulikkk.livehealthierapi.domain.plan.query.PlanQueryRepository;
import lombok.RequiredArgsConstructor;

import java.time.DayOfWeek;

import static java.time.DayOfWeek.*;

@RequiredArgsConstructor
class PlanUpdater {

    private final PlanQueryRepository planQueryRepository;
    private final PlanRepository planRepository;
    private final PlanLevelCalculator planLevelCalculator;
    private final PlanCreator planCreator;
    private final PlanValidator planValidator;

    void updateUserPlanByNewBmiOrAge(String userId, double bmi, int age) {
        PlanDto planDto = planQueryRepository.getPlanByUserId(userId)
                .orElseThrow(() -> new CannotFindPlanException("Nie można znaleźć planu dla takiego użytkownika"));

        DifficultyLevelDto userLevelByNewBmiOrAge = planLevelCalculator.calculateUserLevelByBmiAndAge(bmi, age);

        if (userLevelByNewBmiOrAge != planDto.getUserLevel()) {
            PlanDto newUserPlan = planCreator.createPlanByUserLevel(userId, userLevelByNewBmiOrAge);
            PlanDto newUserPlanWithId = PlanDto.builder()
                    .id(planDto.getId())
                    .userId(userId)
                    .userLevel(newUserPlan.getUserLevel())
                    .availableChangesThiWeek(newUserPlan.getAvailableChangesThiWeek())
                    .monday(newUserPlan.getMonday())
                    .tuesday(newUserPlan.getTuesday())
                    .wednesday(newUserPlan.getWednesday())
                    .thursday(newUserPlan.getThursday())
                    .friday(newUserPlan.getFriday())
                    .saturday(newUserPlan.getSaturday())
                    .sunday(newUserPlan.getSunday())
                    .build();
            planRepository.updateUserPlan(newUserPlanWithId);
        }
    }

    TrainingDto changeTraining(ChangeTrainingCommand changeTrainingCommand) {
            planValidator.validateChangeTrainingCommand(changeTrainingCommand);
        PlanDto planDto = planQueryRepository.getPlanByUserId(changeTrainingCommand.getUserId())
                .orElseThrow(() -> new CannotFindPlanException("Nie można znaleźć planu dla takiego użytkownika"));

        if (changeTrainingCommand.getDayOfWeek() == MONDAY || changeTrainingCommand.getDayOfWeek() == THURSDAY ||
                changeTrainingCommand.getDayOfWeek() == SUNDAY) {
            throw new PlanException("W tych dniach nie ma treningów");
        }

        if (planDto.getAvailableChangesThiWeek() != 0) {
            TrainingDto newTraining = planRepository.getRandomTrainingByDifficultyLevel(changeTrainingCommand.getDifficultyLevelDto());

            switch (changeTrainingCommand.getDayOfWeek()) {
                case TUESDAY:
                    while (planDto.getTuesday().getDescription().equals(newTraining.getDescription())) {
                        newTraining = planRepository.getRandomTrainingByDifficultyLevel(changeTrainingCommand.getDifficultyLevelDto());
                    }
                case WEDNESDAY:
                    while (planDto.getWednesday().getDescription().equals(newTraining.getDescription())) {
                        newTraining = planRepository.getRandomTrainingByDifficultyLevel(changeTrainingCommand.getDifficultyLevelDto());
                    }
                case FRIDAY:
                    while (planDto.getFriday().getDescription().equals(newTraining.getDescription())) {
                        newTraining = planRepository.getRandomTrainingByDifficultyLevel(changeTrainingCommand.getDifficultyLevelDto());
                    }
                case SATURDAY:
                    while (planDto.getSaturday().getDescription().equals(newTraining.getDescription())) {
                        newTraining = planRepository.getRandomTrainingByDifficultyLevel(changeTrainingCommand.getDifficultyLevelDto());
                    }
            }

            PlanDto updatedUserPlan = PlanDto.builder()
                    .id(planDto.getId())
                    .userId(planDto.getUserId())
                    .userLevel(planDto.getUserLevel())
                    .availableChangesThiWeek(planDto.getAvailableChangesThiWeek() - 1)
                    .monday(planDto.getMonday())
                    .tuesday(changeTrainingCommand.getDayOfWeek() == TUESDAY ? newTraining : planDto.getTuesday())
                    .wednesday(changeTrainingCommand.getDayOfWeek() == WEDNESDAY ? newTraining : planDto.getTuesday())
                    .thursday(planDto.getThursday())
                    .friday(changeTrainingCommand.getDayOfWeek() == FRIDAY ? newTraining : planDto.getTuesday())
                    .saturday(changeTrainingCommand.getDayOfWeek() == SATURDAY ? newTraining : planDto.getTuesday())
                    .sunday(planDto.getSunday())
                    .build();

            planRepository.updateUserPlan(updatedUserPlan);

            return newTraining;
        } else {
            throw new PlanException("Nie masz już dostępnych zmian");
        }
    }
}
