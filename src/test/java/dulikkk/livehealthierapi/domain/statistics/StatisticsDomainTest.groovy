package dulikkk.livehealthierapi.domain.statistics

import dulikkk.livehealthierapi.domain.statistics.dto.command.InitialStatisticsCommand
import dulikkk.livehealthierapi.domain.statistics.dto.command.UpdateBmiStatisticsCommand
import dulikkk.livehealthierapi.domain.statistics.dto.command.UpdateHeightStatisticsCommand
import dulikkk.livehealthierapi.domain.statistics.dto.command.UpdateWeightStatisticsCommand
import dulikkk.livehealthierapi.domain.user.dto.UserDto
import dulikkk.livehealthierapi.infrastructure.statistics.memory.InMemoryStatisticsRepository
import dulikkk.livehealthierapi.infrastructure.user.memory.InMemoryUserRepository
import spock.lang.Specification

class StatisticsDomainTest extends Specification {

    private InMemoryStatisticsRepository inMemoryStatisticsRepository = new InMemoryStatisticsRepository()


    private InMemoryUserRepository inMemoryUserRepository = new InMemoryUserRepository() {
        @Override
        Optional<UserDto> findById(String userId) {
            return Optional.of(UserDto.builder().build())
        }
    }

    private StatisticsDomainFacade statisticsDomainFacade = new StatisticsDomainConfigurator().statisticsDomainFacade(
            inMemoryStatisticsRepository, inMemoryUserRepository)

    InitialStatisticsCommand initialStatisticsCommand = new InitialStatisticsCommand("abc123", 170, 76, 23)

    def "initial new statistics"() {
        when: "trying to initial new statistics"
        statisticsDomainFacade.initialStatistics(initialStatisticsCommand)

        then: "the system should add that statistics to repo"
        inMemoryStatisticsRepository.getStatisticsByUserId(initialStatisticsCommand.getUserId()).isPresent()
    }

    def "update bmi statistics"() {
        given: "initialed statistics and new bmi"
        statisticsDomainFacade.initialStatistics(initialStatisticsCommand)
        UpdateBmiStatisticsCommand updateBmiStatisticsCommand = new UpdateBmiStatisticsCommand(
                initialStatisticsCommand.getUserId(), 25)

        when: "trying to update bmi"
        statisticsDomainFacade.updateBmiStatistics(updateBmiStatisticsCommand)

        then: "then the system should update bmi"
        inMemoryStatisticsRepository.getStatisticsByUserId(initialStatisticsCommand.getUserId())
                .get()
                .getBmiStatisticsDto()
                .getLastBmi() == updateBmiStatisticsCommand.getLastCurrentBmi()
    }

    def "update height statistics"() {
        given: "initialed statistics and new height"
        statisticsDomainFacade.initialStatistics(initialStatisticsCommand)
        UpdateHeightStatisticsCommand updateHeightStatisticsCommand = new UpdateHeightStatisticsCommand(
                initialStatisticsCommand.getUserId(), 180)

        when: "trying to update bmi"
        statisticsDomainFacade.updateHeightStatistics(updateHeightStatisticsCommand)

        then: "then the system should update bmi"
        inMemoryStatisticsRepository.getStatisticsByUserId(initialStatisticsCommand.getUserId())
                .get()
                .getHeightStatisticsDto()
                .getLastHeightInCm() == updateHeightStatisticsCommand.getLastCurrentHeightInCm()
    }

    def "update weight statistics"() {
        given: "initialed statistics and new weight"
        statisticsDomainFacade.initialStatistics(initialStatisticsCommand)
        UpdateWeightStatisticsCommand updateWeightStatisticsCommand = new UpdateWeightStatisticsCommand(
                initialStatisticsCommand.getUserId(), 78)

        when: "trying to update bmi"
        statisticsDomainFacade.updateWeightStatistics(updateWeightStatisticsCommand)

        then: "then the system should update bmi"
        inMemoryStatisticsRepository.getStatisticsByUserId(initialStatisticsCommand.getUserId())
                .get()
                .getWeightStatisticsDto()
                .getLastWeightInKg() == updateWeightStatisticsCommand.getLastCurrentWeightInKg()
    }
}
