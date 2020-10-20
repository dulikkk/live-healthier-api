package dulikkk.livehealthierapi.domain.statistics;

import dulikkk.livehealthierapi.domain.statistics.dto.*;
import dulikkk.livehealthierapi.domain.statistics.dto.command.UpdateBmiStatisticsCommand;
import dulikkk.livehealthierapi.domain.statistics.dto.command.UpdateHeightStatisticsCommand;
import dulikkk.livehealthierapi.domain.statistics.dto.command.UpdateWeightStatisticsCommand;
import dulikkk.livehealthierapi.domain.statistics.dto.exception.CannotFindStatisticsException;
import dulikkk.livehealthierapi.domain.statistics.port.outgoing.StatisticsRepository;
import dulikkk.livehealthierapi.domain.statistics.query.StatisticsQueryRepository;
import lombok.RequiredArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static java.time.LocalDate.now;

@RequiredArgsConstructor
class StatisticsUpdater {

    private final StatisticsRepository statisticsRepository;
    private final StatisticsQueryRepository statisticsQueryRepository;

    void updateStatisticsOnNewDay() {
        LocalDate localDate = LocalDate.now();
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        int dayOfMonth = localDate.getDayOfMonth();

        if (dayOfMonth == 1) {
            statisticsRepository.clearMonthStatistics();
        }

        switch (dayOfWeek) {
            case MONDAY: {
                statisticsRepository.clearWeeksStatistics();
            }
            break;
            case TUESDAY:
            case WEDNESDAY:
            case FRIDAY:
            case SATURDAY: {
                statisticsRepository.incrementDaysOfTrainingAndSuperChallenge();
            }
        }
    }

    void updateWeightStatistics(UpdateWeightStatisticsCommand updateWeightStatisticsCommand) {
        StatisticsDto statisticsDto = statisticsQueryRepository
                .getStatisticsByUserId(updateWeightStatisticsCommand.getUserId())
                .orElseThrow(() -> new CannotFindStatisticsException("Nie można znaleźć statystyk dla tego użytkownika"));

        WeightStatisticsDto newWeightStatisticsDto = WeightStatisticsDto.builder()
                .initialWeightInKg(statisticsDto.getWeightStatisticsDto().getInitialWeightInKg())
                .lastWeightInKg(updateWeightStatisticsCommand.getLastCurrentWeightInKg())
                .initialDate(statisticsDto.getWeightStatisticsDto().getInitialDate())
                .lastUpdateDate(now())
                .build();

        StatisticsDto newStatisticsDtoToSave = StatisticsDto.builder()
                .bmiStatisticsDto(statisticsDto.getBmiStatisticsDto())
                .heightStatisticsDto(statisticsDto.getHeightStatisticsDto())
                .weightStatisticsDto(newWeightStatisticsDto)
                .trainingStatisticsDto(statisticsDto.getTrainingStatisticsDto())
                .superChallengeStatisticsDto(statisticsDto.getSuperChallengeStatisticsDto())
                .id(statisticsDto.getId())
                .userId(statisticsDto.getUserId())
                .build();

        statisticsRepository.updateStatistics(newStatisticsDtoToSave);
    }

    void updateHeightStatistics(UpdateHeightStatisticsCommand updateHeightStatisticsCommand){
        StatisticsDto statisticsDto = statisticsQueryRepository
                .getStatisticsByUserId(updateHeightStatisticsCommand.getUserId())
                .orElseThrow(() -> new CannotFindStatisticsException("Nie można znaleźć statystyk dla tego użytkownika"));

        HeightStatisticsDto heightStatisticsDto = HeightStatisticsDto.builder()
                .initialHeightInCm(statisticsDto.getHeightStatisticsDto().getInitialHeightInCm())
                .lastHeightInCm(updateHeightStatisticsCommand.getLastCurrentHeightInCm())
                .initialDate(statisticsDto.getHeightStatisticsDto().getInitialDate())
                .lastUpdateDate(now())
                .build();

        StatisticsDto newStatisticsDtoToSave = StatisticsDto.builder()
                .bmiStatisticsDto(statisticsDto.getBmiStatisticsDto())
                .heightStatisticsDto(heightStatisticsDto)
                .weightStatisticsDto(statisticsDto.getWeightStatisticsDto())
                .trainingStatisticsDto(statisticsDto.getTrainingStatisticsDto())
                .superChallengeStatisticsDto(statisticsDto.getSuperChallengeStatisticsDto())
                .id(statisticsDto.getId())
                .userId(statisticsDto.getUserId())
                .build();

        statisticsRepository.updateStatistics(newStatisticsDtoToSave);
    }

    void updateBmiStatistics(UpdateBmiStatisticsCommand updateBmiStatisticsCommand){
        StatisticsDto statisticsDto = statisticsQueryRepository
                .getStatisticsByUserId(updateBmiStatisticsCommand.getUserId())
                .orElseThrow(() -> new CannotFindStatisticsException("Nie można znaleźć statystyk dla tego użytkownika"));

        BmiStatisticsDto bmiStatisticsDto = BmiStatisticsDto.builder()
                .initialBmi(statisticsDto.getBmiStatisticsDto().getInitialBmi())
                .lastBmi(updateBmiStatisticsCommand.getLastCurrentBmi())
                .initialDate(statisticsDto.getBmiStatisticsDto().getInitialDate())
                .lastUpdateDate(now())
                .build();

        StatisticsDto newStatisticsDtoToSave = StatisticsDto.builder()
                .bmiStatisticsDto(bmiStatisticsDto)
                .heightStatisticsDto(statisticsDto.getHeightStatisticsDto())
                .weightStatisticsDto(statisticsDto.getWeightStatisticsDto())
                .trainingStatisticsDto(statisticsDto.getTrainingStatisticsDto())
                .superChallengeStatisticsDto(statisticsDto.getSuperChallengeStatisticsDto())
                .id(statisticsDto.getId())
                .userId(statisticsDto.getUserId())
                .build();

        statisticsRepository.updateStatistics(newStatisticsDtoToSave);
    }

}
