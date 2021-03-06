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
            clearMonthStatistics();
        }

        switch (dayOfWeek) {
            case MONDAY: {
                clearWeekStatistics();
                todayTrainingDoneTrue();
            }
            case THURSDAY:
            case SUNDAY:{
                todayTrainingDoneTrue();
            }
            break;
            case TUESDAY:
            case WEDNESDAY:
            case FRIDAY:
            case SATURDAY: {
                incrementDaysOfTraining();
            }
        }
        incrementDaysOfSuperChallenges();
    }

    private void todayTrainingDoneTrue() {
        statisticsRepository.getAllStatistics()
                .forEach(this::todayTrainingDoneTrueForSpecificStatisticsDto);
    }

    private void todayTrainingDoneTrueForSpecificStatisticsDto(StatisticsDto statisticsDto) {
        StatisticsDto newStatisticsDtoToSave = StatisticsDto.builder()
                .bmiStatisticsDto(statisticsDto.getBmiStatisticsDto())
                .heightStatisticsDto(statisticsDto.getHeightStatisticsDto())
                .weightStatisticsDto(statisticsDto.getWeightStatisticsDto())
                .trainingStatisticsDto(statisticsDto.getTrainingStatisticsDto())
                .superChallengeStatisticsDto(statisticsDto.getSuperChallengeStatisticsDto())
                .id(statisticsDto.getId())
                .userId(statisticsDto.getUserId())
                .todayTrainingDone(true)
                .todaySuperChallengeDone(statisticsDto.isTodaySuperChallengeDone())
                .build();

        statisticsRepository.updateStatistics(newStatisticsDtoToSave);
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
                .todayTrainingDone(statisticsDto.isTodayTrainingDone())
                .todaySuperChallengeDone(statisticsDto.isTodaySuperChallengeDone())
                .build();

        statisticsRepository.updateStatistics(newStatisticsDtoToSave);
    }

    void updateHeightStatistics(UpdateHeightStatisticsCommand updateHeightStatisticsCommand) {
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
                .todayTrainingDone(statisticsDto.isTodayTrainingDone())
                .todaySuperChallengeDone(statisticsDto.isTodaySuperChallengeDone())
                .build();

        statisticsRepository.updateStatistics(newStatisticsDtoToSave);
    }

    void updateBmiStatistics(UpdateBmiStatisticsCommand updateBmiStatisticsCommand) {
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
                .todayTrainingDone(statisticsDto.isTodayTrainingDone())
                .todaySuperChallengeDone(statisticsDto.isTodaySuperChallengeDone())
                .build();

        statisticsRepository.updateStatistics(newStatisticsDtoToSave);
    }

    void doneTraining(String userId) {
        StatisticsDto statisticsDto = statisticsQueryRepository
                .getStatisticsByUserId(userId)
                .orElseThrow(() -> new CannotFindStatisticsException("Nie można znaleźć statystyk dla tego użytkownika"));

        TrainingStatisticsDto oldTrainingStatisticsDto = statisticsDto.getTrainingStatisticsDto();
        TrainingStatisticsDto trainingStatisticsDto = TrainingStatisticsDto.builder()
                .doneTrainingsThisWeek(oldTrainingStatisticsDto.getDoneTrainingsThisWeek() + 1)
                .allTrainingsThisWeek(oldTrainingStatisticsDto.getAllTrainingsThisWeek())
                .doneTrainingsThisMonth(oldTrainingStatisticsDto.getDoneTrainingsThisMonth() + 1)
                .allTrainingsThisMonth(oldTrainingStatisticsDto.getAllTrainingsThisMonth())
                .doneAllTrainings(oldTrainingStatisticsDto.getDoneAllTrainings() + 1)
                .allTrainings(oldTrainingStatisticsDto.getAllTrainings())
                .build();

        StatisticsDto updatedStatistics = StatisticsDto.builder()
                .id(statisticsDto.getId())
                .userId(statisticsDto.getUserId())
                .bmiStatisticsDto(statisticsDto.getBmiStatisticsDto())
                .heightStatisticsDto(statisticsDto.getHeightStatisticsDto())
                .weightStatisticsDto(statisticsDto.getWeightStatisticsDto())
                .trainingStatisticsDto(trainingStatisticsDto)
                .superChallengeStatisticsDto(statisticsDto.getSuperChallengeStatisticsDto())
                .todayTrainingDone(true)
                .todaySuperChallengeDone(statisticsDto.isTodaySuperChallengeDone())
                .build();

        statisticsRepository.updateStatistics(updatedStatistics);
    }

    void doneSuperChallenge(String userId) {
        StatisticsDto statisticsDto = statisticsQueryRepository
                .getStatisticsByUserId(userId)
                .orElseThrow(() -> new CannotFindStatisticsException("Nie można znaleźć statystyk dla tego użytkownika"));

        SuperChallengeStatisticsDto oldSuperChallengeStatisticsDto = statisticsDto.getSuperChallengeStatisticsDto();
        SuperChallengeStatisticsDto superChallengeStatisticsDto = SuperChallengeStatisticsDto.builder()
                .allSuperChallengesThisWeek(oldSuperChallengeStatisticsDto.getAllSuperChallenges())
                .doneSuperChallengesThisWeek(oldSuperChallengeStatisticsDto.getDoneSuperChallengesThisWeek() + 1)
                .allSuperChallengesThisMonth(oldSuperChallengeStatisticsDto.getAllSuperChallengesThisMonth())
                .doneSuperChallengesThisMonth(oldSuperChallengeStatisticsDto.getDoneSuperChallengesThisMonth() + 1)
                .doneAllSuperChallenges(oldSuperChallengeStatisticsDto.getDoneAllSuperChallenges() + 1)
                .allSuperChallenges(oldSuperChallengeStatisticsDto.getAllSuperChallenges())
                .build();

        StatisticsDto updatedStatistics = StatisticsDto.builder()
                .id(statisticsDto.getId())
                .userId(statisticsDto.getUserId())
                .bmiStatisticsDto(statisticsDto.getBmiStatisticsDto())
                .heightStatisticsDto(statisticsDto.getHeightStatisticsDto())
                .weightStatisticsDto(statisticsDto.getWeightStatisticsDto())
                .trainingStatisticsDto(statisticsDto.getTrainingStatisticsDto())
                .superChallengeStatisticsDto(superChallengeStatisticsDto)
                .todayTrainingDone(statisticsDto.isTodayTrainingDone())
                .todaySuperChallengeDone(true)
                .build();

        statisticsRepository.updateStatistics(updatedStatistics);
    }

    private void clearMonthStatistics() {
        statisticsRepository.getAllStatistics()
                .forEach(this::clearMonthStatisticsForSpecificStatisticsDto);
    }

    private void clearMonthStatisticsForSpecificStatisticsDto(StatisticsDto statisticsDto) {
        TrainingStatisticsDto oldTrainingStatisticsDto = statisticsDto.getTrainingStatisticsDto();
        TrainingStatisticsDto trainingStatisticsDto = TrainingStatisticsDto.builder()
                .doneTrainingsThisWeek(oldTrainingStatisticsDto.getDoneTrainingsThisWeek())
                .allTrainingsThisWeek(oldTrainingStatisticsDto.getAllTrainingsThisWeek())
                .doneTrainingsThisMonth(0)
                .allTrainingsThisMonth(0)
                .doneAllTrainings(oldTrainingStatisticsDto.getDoneAllTrainings())
                .allTrainings(oldTrainingStatisticsDto.getAllTrainings())
                .build();

        SuperChallengeStatisticsDto oldSuperChallengeStatisticsDto = statisticsDto.getSuperChallengeStatisticsDto();
        SuperChallengeStatisticsDto superChallengeStatisticsDto = SuperChallengeStatisticsDto.builder()
                .allSuperChallengesThisWeek(oldSuperChallengeStatisticsDto.getAllSuperChallenges())
                .doneSuperChallengesThisWeek(oldSuperChallengeStatisticsDto.getDoneSuperChallengesThisWeek())
                .allSuperChallengesThisMonth(0)
                .doneSuperChallengesThisMonth(0)
                .doneAllSuperChallenges(oldSuperChallengeStatisticsDto.getDoneAllSuperChallenges())
                .allSuperChallenges(oldSuperChallengeStatisticsDto.getAllSuperChallenges())
                .build();

        StatisticsDto updatedStatistics = StatisticsDto.builder()
                .id(statisticsDto.getId())
                .userId(statisticsDto.getUserId())
                .bmiStatisticsDto(statisticsDto.getBmiStatisticsDto())
                .heightStatisticsDto(statisticsDto.getHeightStatisticsDto())
                .weightStatisticsDto(statisticsDto.getWeightStatisticsDto())
                .trainingStatisticsDto(trainingStatisticsDto)
                .superChallengeStatisticsDto(superChallengeStatisticsDto)
                .build();

        // TODO potential bottleneck
        statisticsRepository.updateStatistics(updatedStatistics);
    }

    private void clearWeekStatistics() {
        statisticsRepository.getAllStatistics()
                .forEach(this::clearWeekStatisticsForSpecificStatisticsDto);
    }

    private void clearWeekStatisticsForSpecificStatisticsDto(StatisticsDto statisticsDto) {
        TrainingStatisticsDto oldTrainingStatisticsDto = statisticsDto.getTrainingStatisticsDto();
        TrainingStatisticsDto trainingStatisticsDto = TrainingStatisticsDto.builder()
                .doneTrainingsThisWeek(0)
                .allTrainingsThisWeek(0)
                .doneTrainingsThisMonth(oldTrainingStatisticsDto.getDoneTrainingsThisMonth())
                .allTrainingsThisMonth(oldTrainingStatisticsDto.getAllTrainingsThisMonth())
                .doneAllTrainings(oldTrainingStatisticsDto.getDoneAllTrainings())
                .allTrainings(oldTrainingStatisticsDto.getAllTrainings())
                .build();

        SuperChallengeStatisticsDto oldSuperChallengeStatisticsDto = statisticsDto.getSuperChallengeStatisticsDto();
        SuperChallengeStatisticsDto superChallengeStatisticsDto = SuperChallengeStatisticsDto.builder()
                .allSuperChallengesThisWeek(0)
                .doneSuperChallengesThisWeek(0)
                .allSuperChallengesThisMonth(oldSuperChallengeStatisticsDto.getAllSuperChallengesThisMonth())
                .doneSuperChallengesThisMonth(oldSuperChallengeStatisticsDto.getDoneSuperChallengesThisMonth())
                .doneAllSuperChallenges(oldSuperChallengeStatisticsDto.getDoneAllSuperChallenges())
                .allSuperChallenges(oldSuperChallengeStatisticsDto.getAllSuperChallenges())
                .build();

        StatisticsDto updatedStatistics = StatisticsDto.builder()
                .id(statisticsDto.getId())
                .userId(statisticsDto.getUserId())
                .bmiStatisticsDto(statisticsDto.getBmiStatisticsDto())
                .heightStatisticsDto(statisticsDto.getHeightStatisticsDto())
                .weightStatisticsDto(statisticsDto.getWeightStatisticsDto())
                .trainingStatisticsDto(trainingStatisticsDto)
                .superChallengeStatisticsDto(superChallengeStatisticsDto)
                .build();

        // TODO potential bottleneck
        statisticsRepository.updateStatistics(updatedStatistics);
    }

    private void incrementDaysOfTraining() {
        statisticsRepository.getAllStatistics()
                .forEach(this::incrementDaysOfTrainingForSpecificStatisticsDto);
    }

    private void incrementDaysOfSuperChallenges(){
        statisticsRepository.getAllStatistics()
                .forEach(this::incrementDaysOfSuperChallengesForSpecificStatisticsDto);
    }

    private void incrementDaysOfTrainingForSpecificStatisticsDto(StatisticsDto statisticsDto) {
        TrainingStatisticsDto oldTrainingStatisticsDto = statisticsDto.getTrainingStatisticsDto();
        TrainingStatisticsDto trainingStatisticsDto = TrainingStatisticsDto.builder()
                .doneTrainingsThisWeek(oldTrainingStatisticsDto.getDoneTrainingsThisWeek())
                .allTrainingsThisWeek(oldTrainingStatisticsDto.getAllTrainingsThisWeek() + 1)
                .doneTrainingsThisMonth(oldTrainingStatisticsDto.getDoneTrainingsThisMonth())
                .allTrainingsThisMonth(oldTrainingStatisticsDto.getAllTrainingsThisMonth() + 1)
                .doneAllTrainings(oldTrainingStatisticsDto.getDoneAllTrainings())
                .allTrainings(oldTrainingStatisticsDto.getAllTrainings() + 1)
                .build();

        StatisticsDto updatedStatistics = StatisticsDto.builder()
                .id(statisticsDto.getId())
                .userId(statisticsDto.getUserId())
                .bmiStatisticsDto(statisticsDto.getBmiStatisticsDto())
                .heightStatisticsDto(statisticsDto.getHeightStatisticsDto())
                .weightStatisticsDto(statisticsDto.getWeightStatisticsDto())
                .trainingStatisticsDto(trainingStatisticsDto)
                .superChallengeStatisticsDto(statisticsDto.getSuperChallengeStatisticsDto())
                .todaySuperChallengeDone(statisticsDto.isTodaySuperChallengeDone())
                .todayTrainingDone(false)
                .build();

        // TODO potential bottleneck
        statisticsRepository.updateStatistics(updatedStatistics);
    }

    private void incrementDaysOfSuperChallengesForSpecificStatisticsDto(StatisticsDto statisticsDto){
        SuperChallengeStatisticsDto oldSuperChallengeStatisticsDto = statisticsDto.getSuperChallengeStatisticsDto();
        SuperChallengeStatisticsDto superChallengeStatisticsDto = SuperChallengeStatisticsDto.builder()
                .allSuperChallengesThisWeek(oldSuperChallengeStatisticsDto.getAllSuperChallengesThisWeek() + 1)
                .doneSuperChallengesThisWeek(oldSuperChallengeStatisticsDto.getDoneSuperChallengesThisWeek())
                .allSuperChallengesThisMonth(oldSuperChallengeStatisticsDto.getAllSuperChallengesThisMonth() + 1)
                .doneSuperChallengesThisMonth(oldSuperChallengeStatisticsDto.getDoneSuperChallengesThisMonth())
                .allSuperChallenges(oldSuperChallengeStatisticsDto.getAllSuperChallenges() + 1)
                .doneAllSuperChallenges(oldSuperChallengeStatisticsDto.getDoneAllSuperChallenges())
                .build();

        StatisticsDto updatedStatistics = StatisticsDto.builder()
                .id(statisticsDto.getId())
                .userId(statisticsDto.getUserId())
                .bmiStatisticsDto(statisticsDto.getBmiStatisticsDto())
                .heightStatisticsDto(statisticsDto.getHeightStatisticsDto())
                .weightStatisticsDto(statisticsDto.getWeightStatisticsDto())
                .trainingStatisticsDto(statisticsDto.getTrainingStatisticsDto())
                .superChallengeStatisticsDto(superChallengeStatisticsDto)
                .todaySuperChallengeDone(false)
                .todayTrainingDone(statisticsDto.isTodayTrainingDone())
                .build();

        // TODO potential bottleneck
        statisticsRepository.updateStatistics(updatedStatistics);
    }

}
