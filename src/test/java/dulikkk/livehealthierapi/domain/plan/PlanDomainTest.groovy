package dulikkk.livehealthierapi.domain.plan

import dulikkk.livehealthierapi.domain.plan.dto.NewPlanCommand
import dulikkk.livehealthierapi.domain.user.dto.UserDto
import dulikkk.livehealthierapi.infrastructure.plan.memory.InMemoryPlanRepository
import dulikkk.livehealthierapi.infrastructure.user.memory.InMemoryUserRepository
import spock.lang.Specification

import static dulikkk.livehealthierapi.domain.plan.dto.DifficultyLevelDto.*

class PlanDomainTest extends Specification {

    private InMemoryPlanRepository inMemoryPlanRepository = new InMemoryPlanRepository()

    private InMemoryUserRepository inMemoryUserRepository = new InMemoryUserRepository() {
        @Override
        Optional<UserDto> findById(String userId) {
            return Optional.of(UserDto.builder().build())
        }
    }

    private PlanDomainFacade planDomainFacade = new PlanDomainConfigurator().planDomainFacade(inMemoryPlanRepository, inMemoryUserRepository)

    def "calculate user level"() {
        given: "17 years old user with BMI 21"
        NewPlanCommand newPlanCommand = new NewPlanCommand("abc123", 21, 17)

        when: "trying to create new plan for this user"
        planDomainFacade.createPlanForUser(newPlanCommand)

        then: "the system should calculate A1 level"
        inMemoryPlanRepository.getPlanByUserId(newPlanCommand.getUserId()).getUserLevel() == A1
        String userId = newPlanCommand.getUserId()
        inMemoryPlanRepository.getPlanByUserId(newPlanCommand.getUserId()).getUserLevel() == A1
        inMemoryPlanRepository.getPlanByUserId(userId).getTuesday().getTrainingDifficultyDto() == A2
        inMemoryPlanRepository.getPlanByUserId(userId).getWednesday().getTrainingDifficultyDto() == A1
        inMemoryPlanRepository.getPlanByUserId(userId).getFriday().getTrainingDifficultyDto() == A1
        inMemoryPlanRepository.getPlanByUserId(userId).getSaturday().getTrainingDifficultyDto() == A1
    }

    def "calculate level for old user"() {
        given: "61 years old user with BMI 20"
        NewPlanCommand newPlanCommandForOldUser = new NewPlanCommand("abc123", 20, 61)

        when: "trying to create new plan for this user"
        planDomainFacade.createPlanForUser(newPlanCommandForOldUser)

        then: "the system should calculate C2 level"
        inMemoryPlanRepository.getPlanByUserId(newPlanCommandForOldUser.getUserId()).getUserLevel() == C2
    }

    def "update bmi"() {
        given: "17 years old user with BMI 20"
        NewPlanCommand newPlanCommand = new NewPlanCommand("abc123", 20, 17)
        planDomainFacade.createPlanForUser(newPlanCommand)

        when: "when the user changes him BMI to 30"
        planDomainFacade.updatePlanByNewBmiOrAge(newPlanCommand.getUserId(), 30, newPlanCommand.getAge())

        then: "the system should calculate a new level and update plan by a new level"
        String userId = newPlanCommand.getUserId()
        inMemoryPlanRepository.getPlanByUserId(newPlanCommand.getUserId()).getUserLevel() == B2
        inMemoryPlanRepository.getPlanByUserId(userId).getTuesday().getTrainingDifficultyDto() == B1
        inMemoryPlanRepository.getPlanByUserId(userId).getWednesday().getTrainingDifficultyDto() == B2
        inMemoryPlanRepository.getPlanByUserId(userId).getFriday().getTrainingDifficultyDto() == C1
        inMemoryPlanRepository.getPlanByUserId(userId).getSaturday().getTrainingDifficultyDto() == B2


    }

}
