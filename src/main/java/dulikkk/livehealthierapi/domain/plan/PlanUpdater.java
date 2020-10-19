package dulikkk.livehealthierapi.domain.plan;

import dulikkk.livehealthierapi.domain.plan.dto.DifficultyLevelDto;
import dulikkk.livehealthierapi.domain.plan.dto.PlanDto;
import dulikkk.livehealthierapi.domain.plan.port.outgoing.PlanRepository;
import dulikkk.livehealthierapi.domain.plan.query.PlanQueryRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class PlanUpdater {

    private final PlanQueryRepository planQueryRepository;
    private final PlanRepository planRepository;
    private final PlanLevelCalculator planLevelCalculator;
    private final PlanCreator planCreator;

    void updateUserPlanByNewBmiOrAge(String userId, double bmi, int age){
        DifficultyLevelDto currentUserLevel = planQueryRepository.getPlanByUserId(userId)
                .getUserLevel();

        DifficultyLevelDto userLevelByNewBmiOrAge = planLevelCalculator.calculateUserLevelByBmiAndAge(bmi, age);

        if(userLevelByNewBmiOrAge != currentUserLevel){
            PlanDto newUserPlan = planCreator.createPlanByUserLevel(userId, userLevelByNewBmiOrAge);
            PlanDto newUserPlanWithId = PlanDto.builder()
                    .id(planQueryRepository.getPlanByUserId(userId).getId())
                    .userId(userId)
                    .userLevel(newUserPlan.getUserLevel())
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
}
