package dulikkk.livehealthierapi.domain.plan;

import dulikkk.livehealthierapi.domain.plan.dto.DifficultyLevelDto;
import dulikkk.livehealthierapi.domain.plan.dto.NewPlanCommand;
import dulikkk.livehealthierapi.domain.plan.dto.PlanDto;
import dulikkk.livehealthierapi.domain.plan.dto.TrainingDto;
import dulikkk.livehealthierapi.domain.plan.port.outgoing.PlanRepository;
import dulikkk.livehealthierapi.domain.user.dto.exception.CannotFindUserException;
import dulikkk.livehealthierapi.domain.user.query.UserQueryRepository;
import lombok.RequiredArgsConstructor;

import static dulikkk.livehealthierapi.domain.plan.dto.DifficultyLevelDto.*;

@RequiredArgsConstructor
class PlanCreator {

    private final PlanRepository trainingRepository;
    private final PlanLevelCalculator planLevelCalculator;
    private final UserQueryRepository userQueryRepository;

    void createPlan(NewPlanCommand newPlanCommand) {
        isThisUserExists(newPlanCommand.getUserId());
        DifficultyLevelDto userLevel = planLevelCalculator.calculateUserLevelByBmiAndAge(newPlanCommand.getBmi(), newPlanCommand.getAge());

        PlanDto userPlan = createPlanByUserLevel(newPlanCommand.getUserId(), userLevel);

        trainingRepository.savePlan(userPlan);
    }

    PlanDto createPlanByUserLevel(String userId, DifficultyLevelDto userLevel) {
        TrainingDto firstTraining = null;
        TrainingDto secondTraining = null;
        TrainingDto thirdTraining = null;
        TrainingDto fourthTraining = null;

        TrainingDto breakDay = trainingRepository.getBreakDay();

        switch (userLevel) {
            case A1: {
                firstTraining = trainingRepository.getRandomTrainingByDifficultyLevel(A2);
                secondTraining = trainingRepository.getRandomTrainingByDifficultyLevel(A1);
                thirdTraining = trainingRepository.getRandomTrainingByDifficultyLevel(A1);
                fourthTraining = trainingRepository.getRandomTrainingByDifficultyLevel(A1);
            }
            break;
            case A2: {
                firstTraining = trainingRepository.getRandomTrainingByDifficultyLevel(A1);
                secondTraining = trainingRepository.getRandomTrainingByDifficultyLevel(A2);
                thirdTraining = trainingRepository.getRandomTrainingByDifficultyLevel(B1);
                fourthTraining = trainingRepository.getRandomTrainingByDifficultyLevel(A2);
            }
            break;

            case B1: {
                firstTraining = trainingRepository.getRandomTrainingByDifficultyLevel(A2);
                secondTraining = trainingRepository.getRandomTrainingByDifficultyLevel(B1);
                thirdTraining = trainingRepository.getRandomTrainingByDifficultyLevel(B2);
                fourthTraining = trainingRepository.getRandomTrainingByDifficultyLevel(B1);
            }
            break;

            case B2: {
                firstTraining = trainingRepository.getRandomTrainingByDifficultyLevel(B1);
                secondTraining = trainingRepository.getRandomTrainingByDifficultyLevel(B2);
                thirdTraining = trainingRepository.getRandomTrainingByDifficultyLevel(C1);
                fourthTraining = trainingRepository.getRandomTrainingByDifficultyLevel(B2);
            }
            break;

            case C1: {
                firstTraining = trainingRepository.getRandomTrainingByDifficultyLevel(B2);
                secondTraining = trainingRepository.getRandomTrainingByDifficultyLevel(C1);
                thirdTraining = trainingRepository.getRandomTrainingByDifficultyLevel(C2);
                fourthTraining = trainingRepository.getRandomTrainingByDifficultyLevel(C1);
            }
            break;

            case C2: {
                firstTraining = trainingRepository.getRandomTrainingByDifficultyLevel(C1);
                secondTraining = trainingRepository.getRandomTrainingByDifficultyLevel(C2);
                thirdTraining = trainingRepository.getRandomTrainingByDifficultyLevel(C2);
                fourthTraining = trainingRepository.getRandomTrainingByDifficultyLevel(C2);
            }
        }

        return PlanDto.builder()
                .userId(userId)
                .userLevel(userLevel)
                .monday(breakDay)
                .tuesday(firstTraining)
                .wednesday(secondTraining)
                .thursday(breakDay)
                .friday(thirdTraining)
                .saturday(fourthTraining)
                .sunday(breakDay)
                .build();
    }

    void isThisUserExists(String userId){
        userQueryRepository.findById(userId)
                .orElseThrow(() -> new CannotFindUserException("Nie ma takiego użytkownika"));
    }

}
