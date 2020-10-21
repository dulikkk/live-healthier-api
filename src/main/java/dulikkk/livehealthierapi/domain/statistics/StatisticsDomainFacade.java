package dulikkk.livehealthierapi.domain.statistics;

import dulikkk.livehealthierapi.domain.statistics.dto.command.InitialStatisticsCommand;
import dulikkk.livehealthierapi.domain.statistics.dto.command.UpdateBmiStatisticsCommand;
import dulikkk.livehealthierapi.domain.statistics.dto.command.UpdateHeightStatisticsCommand;
import dulikkk.livehealthierapi.domain.statistics.dto.command.UpdateWeightStatisticsCommand;
import dulikkk.livehealthierapi.domain.statistics.port.outgoing.StatisticsRepository;
import dulikkk.livehealthierapi.domain.statistics.query.StatisticsQueryRepository;
import dulikkk.livehealthierapi.domain.user.query.UserQueryRepository;

public class StatisticsDomainFacade {

    private final StatisticsCreator statisticsCreator;
    private final StatisticsUpdater statisticsUpdater;

    public StatisticsDomainFacade(StatisticsRepository statisticsRepository,
                                  StatisticsQueryRepository statisticsQueryRepository, UserQueryRepository userQueryRepository) {
        this.statisticsCreator = new StatisticsCreator(statisticsRepository, userQueryRepository);
        this.statisticsUpdater = new StatisticsUpdater(statisticsRepository, statisticsQueryRepository);
    }

    public void initialStatistics(InitialStatisticsCommand initialStatisticsCommand) {
        statisticsCreator.initialStatistics(initialStatisticsCommand);
    }

    public void updateStatisticsOnNewDay() {
        statisticsUpdater.updateStatisticsOnNewDay();
    }

    public void doneTraining(String userId) {
        statisticsUpdater.doneTraining(userId);
    }

    public void doneSuperChallenge(String userId) {
        statisticsUpdater.doneSuperChallenge(userId);
    }

    public void updateBmiStatistics(UpdateBmiStatisticsCommand updateBmiStatisticsCommand) {
        statisticsUpdater.updateBmiStatistics(updateBmiStatisticsCommand);
    }

    public void updateHeightStatistics(UpdateHeightStatisticsCommand updateHeightStatisticsCommand) {
        statisticsUpdater.updateHeightStatistics(updateHeightStatisticsCommand);
    }

    public void updateWeightStatistics(UpdateWeightStatisticsCommand updateWeightStatisticsCommand) {
        statisticsUpdater.updateWeightStatistics(updateWeightStatisticsCommand);
    }
}
