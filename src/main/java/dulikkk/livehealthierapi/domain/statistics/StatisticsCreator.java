package dulikkk.livehealthierapi.domain.statistics;

import dulikkk.livehealthierapi.domain.statistics.dto.*;
import dulikkk.livehealthierapi.domain.statistics.dto.command.InitialStatisticsCommand;
import dulikkk.livehealthierapi.domain.statistics.port.outgoing.StatisticsRepository;
import dulikkk.livehealthierapi.domain.user.dto.exception.CannotFindUserException;
import dulikkk.livehealthierapi.domain.user.query.UserQueryRepository;
import lombok.RequiredArgsConstructor;

import static java.time.LocalDate.now;

@RequiredArgsConstructor
class StatisticsCreator {

    private final StatisticsRepository statisticsRepository;
    private final UserQueryRepository userQueryRepository;

    void initialStatistics(InitialStatisticsCommand initialStatisticsCommand) {
        isThisUserExists(initialStatisticsCommand.getUserId());

        HeightStatisticsDto heightStatisticsDto = HeightStatisticsDto.builder()
                .initialHeightInCm(initialStatisticsCommand.getHeightInCm())
                .lastHeightInCm(initialStatisticsCommand.getHeightInCm())
                .initialDate(now())
                .lastUpdateDate(now())
                .build();

        WeightStatisticsDto weightStatisticsDto = WeightStatisticsDto.builder()
                .initialWeightInKg(initialStatisticsCommand.getWeightInKg())
                .lastWeightInKg(initialStatisticsCommand.getWeightInKg())
                .initialDate(now())
                .lastUpdateDate(now())
                .build();

        BmiStatisticsDto bmiStatisticsDto = BmiStatisticsDto.builder()
                .initialBmi(initialStatisticsCommand.getBmi())
                .lastBmi(initialStatisticsCommand.getBmi())
                .initialDate(now())
                .lastUpdateDate(now())
                .build();

        TrainingStatisticsDto trainingStatisticsDto = TrainingStatisticsDto.builder()
                .allTrainings(0)
                .allTrainingsThisMonth(0)
                .allTrainingsThisWeek(0)
                .doneAllTrainings(0)
                .doneTrainingsThisMonth(0)
                .doneTrainingsThisWeek(0)
                .build();

        SuperChallengeStatisticsDto superChallengeStatisticsDto = SuperChallengeStatisticsDto.builder()
                .allSuperChallenges(0)
                .allSuperChallengesThisMonth(0)
                .allSuperChallengesThisWeek(0)
                .doneAllSuperChallenges(0)
                .doneSuperChallengesThisMonth(0)
                .doneSuperChallengesThisWeek(0)
                .build();

        StatisticsDto statisticsDto = StatisticsDto.builder()
                .userId(initialStatisticsCommand.getUserId())
                .heightStatisticsDto(heightStatisticsDto)
                .weightStatisticsDto(weightStatisticsDto)
                .bmiStatisticsDto(bmiStatisticsDto)
                .trainingStatisticsDto(trainingStatisticsDto)
                .superChallengeStatisticsDto(superChallengeStatisticsDto)
                .build();

        statisticsRepository.saveStatistics(statisticsDto);
    }

    private void isThisUserExists(String userId) {
        userQueryRepository.findById(userId)
                .orElseThrow(() -> new CannotFindUserException("Nie ma takiego użytkownika"));
    }
}
