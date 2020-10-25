package dulikkk.livehealthierapi.domain.statistics;

import dulikkk.livehealthierapi.domain.statistics.dto.*;
import dulikkk.livehealthierapi.domain.statistics.dto.command.InitialStatisticsCommand;
import dulikkk.livehealthierapi.domain.statistics.port.outgoing.StatisticsRepository;
import dulikkk.livehealthierapi.domain.user.dto.exception.CannotFindUserException;
import dulikkk.livehealthierapi.domain.user.query.UserQueryRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

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

        LocalDate localDate = LocalDate.now();
        int initialTrainings = 0;
        boolean todayTrainingDone = true;
        switch(localDate.getDayOfWeek()){
            case TUESDAY:
            case WEDNESDAY:
            case FRIDAY:
            case SATURDAY:{
                initialTrainings = 1;
                todayTrainingDone= false;
            }
        }

        TrainingStatisticsDto trainingStatisticsDto = TrainingStatisticsDto.builder()
                .allTrainings(initialTrainings)
                .allTrainingsThisMonth(initialTrainings)
                .allTrainingsThisWeek(initialTrainings)
                .doneAllTrainings(0)
                .doneTrainingsThisMonth(0)
                .doneTrainingsThisWeek(0)
                .build();

        SuperChallengeStatisticsDto superChallengeStatisticsDto = SuperChallengeStatisticsDto.builder()
                .allSuperChallenges(1)
                .allSuperChallengesThisMonth(1)
                .allSuperChallengesThisWeek(1)
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
                .todayTrainingDone(todayTrainingDone)
                .todaySuperChallengeDone(false)
                .build();

        statisticsRepository.saveStatistics(statisticsDto);
    }

    private void isThisUserExists(String userId) {
        userQueryRepository.findById(userId)
                .orElseThrow(() -> new CannotFindUserException("Nie ma takiego użytkownika"));
    }
}
