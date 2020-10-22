package dulikkk.livehealthierapi.domain.plan;

import dulikkk.livehealthierapi.domain.plan.dto.TrainingDto;
import dulikkk.livehealthierapi.domain.plan.dto.command.ChangeTrainingCommand;
import dulikkk.livehealthierapi.domain.plan.dto.command.NewPlanCommand;
import dulikkk.livehealthierapi.domain.plan.port.outgoing.PlanRepository;
import dulikkk.livehealthierapi.domain.plan.query.PlanQueryRepository;
import dulikkk.livehealthierapi.domain.user.query.UserQueryRepository;

public class PlanDomainFacade {

    private final PlanCreator planCreator;
    private final PlanUpdater planUpdater;

    public PlanDomainFacade(PlanRepository planRepository, PlanQueryRepository planQueryRepository, UserQueryRepository userQueryRepository) {
        PlanLevelCalculator planLevelCalculator = new PlanLevelCalculator();
        PlanValidator planValidator = new PlanValidator();
        this.planCreator = new PlanCreator(planRepository, planLevelCalculator, userQueryRepository);
        this.planUpdater = new PlanUpdater(planQueryRepository, planRepository, planLevelCalculator,
                planCreator, planValidator);
    }

    public void createPlanForUser(NewPlanCommand newPlanCommand) {
        planCreator.createPlan(newPlanCommand);
    }

    public void updatePlanByNewBmiOrAge(String userId, double bmi, int age) {
        planUpdater.updateUserPlanByNewBmiOrAge(userId, bmi, age);
    }

    public TrainingDto changeTraining(ChangeTrainingCommand changeTrainingCommand) {
        return planUpdater.changeTraining(changeTrainingCommand);
    }
}
