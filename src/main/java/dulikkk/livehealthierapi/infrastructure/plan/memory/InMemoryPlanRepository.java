package dulikkk.livehealthierapi.infrastructure.plan.memory;

import dulikkk.livehealthierapi.domain.plan.dto.DifficultyLevelDto;
import dulikkk.livehealthierapi.domain.plan.dto.PlanDto;
import dulikkk.livehealthierapi.domain.plan.dto.TrainingDto;
import dulikkk.livehealthierapi.domain.plan.dto.exception.CannotFindPlanException;
import dulikkk.livehealthierapi.domain.plan.dto.exception.PlanException;
import dulikkk.livehealthierapi.domain.plan.port.outgoing.PlanRepository;
import dulikkk.livehealthierapi.domain.plan.query.PlanQueryRepository;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static dulikkk.livehealthierapi.domain.plan.dto.DifficultyLevelDto.*;
import static dulikkk.livehealthierapi.domain.plan.dto.TrainingTypeDto.*;
import static java.util.UUID.*;

public class InMemoryPlanRepository implements PlanRepository, PlanQueryRepository {

    private final Map<String, TrainingDto> trainingsRepo = loadTrainingsRepo();
    private final Map<String, PlanDto> plansRepo = new ConcurrentHashMap<>();

    private Map<String, TrainingDto> loadTrainingsRepo() {
        Map<String, TrainingDto> repo = new ConcurrentHashMap<>();
        TrainingDto firstTrainingA1 = TrainingDto.builder()
                .id(String.valueOf(randomUUID()))
                .trainingDifficultyDto(A1)
                .trainingTypeDto(INDOOR_EXERCISES)
                .description("Ćwiczenia w domu. Wykonaj dzisiaj taki set: *Przykładowy set A1*")
                .build();

        TrainingDto secondTrainingA1 = TrainingDto.builder()
                .id(String.valueOf(randomUUID()))
                .trainingDifficultyDto(A1)
                .trainingTypeDto(RUNNING)
                .description("Przebiegnij dzisiaj 5km tak szybko jak możesz")
                .build();

        TrainingDto firstTrainingA2 = TrainingDto.builder()
                .id(String.valueOf(randomUUID()))
                .trainingDifficultyDto(A2)
                .trainingTypeDto(INDOOR_EXERCISES)
                .description("Ćwiczenia w domu. Wykonaj dzisiaj taki set: *Przykładowy set A2*")
                .build();

        TrainingDto secondTrainingA2 = TrainingDto.builder()
                .id(String.valueOf(randomUUID()))
                .trainingDifficultyDto(A2)
                .trainingTypeDto(RUNNING)
                .description("Przebiegnij dzisiaj 3km tak szybko jak możesz")
                .build();

        TrainingDto firstTrainingB1 = TrainingDto.builder()
                .id(String.valueOf(randomUUID()))
                .trainingDifficultyDto(B1)
                .trainingTypeDto(STRETCHING)
                .description("Rozciąganie. Wykonaj dzisiaj taki set rozciągania: *Przykładowy set rozciągania B1*")
                .build();

        TrainingDto secondTrainingB1 = TrainingDto.builder()
                .id(String.valueOf(randomUUID()))
                .trainingDifficultyDto(B1)
                .trainingTypeDto(RUNNING)
                .description("Przebiegnij dzisiaj 1.5km tak szybko jak możesz")
                .build();

        TrainingDto firstTrainingB2 = TrainingDto.builder()
                .id(String.valueOf(randomUUID()))
                .trainingDifficultyDto(B2)
                .trainingTypeDto(WALKING)
                .description("Pospaceruj dzisiaj przez 1.5h")
                .build();

        TrainingDto secondTrainingB2 = TrainingDto.builder()
                .id(String.valueOf(randomUUID()))
                .trainingDifficultyDto(B2)
                .trainingTypeDto(OUTDOOR_EXERCISES)
                .description("Ćwiczenia na dworze. Wykonaj dzisiaj taki set: *Przykładowy set B2*")
                .build();

        TrainingDto firstTrainingC1 = TrainingDto.builder()
                .id(String.valueOf(randomUUID()))
                .trainingDifficultyDto(C1)
                .trainingTypeDto(WALKING)
                .description("Pospaceruj dzisiaj przez 1h")
                .build();

        TrainingDto secondTrainingC1 = TrainingDto.builder()
                .id(String.valueOf(randomUUID()))
                .trainingDifficultyDto(C1)
                .trainingTypeDto(INDOOR_EXERCISES)
                .description("Ćwiczenia w domu. Wykonaj dzisiaj taki set: *Przykładowy set C1*")
                .build();

        TrainingDto firstTrainingC2 = TrainingDto.builder()
                .id(String.valueOf(randomUUID()))
                .trainingDifficultyDto(C2)
                .trainingTypeDto(WALKING)
                .description("Pospaceruj dzisiaj przez 45 minut")
                .build();

        TrainingDto secondTrainingC2 = TrainingDto.builder()
                .id(String.valueOf(randomUUID()))
                .trainingDifficultyDto(C2)
                .trainingTypeDto(INDOOR_EXERCISES)
                .description("Ćwiczenia w domu. Wykonaj dzisiaj taki set: *Przykładowy set C2*")
                .build();

        TrainingDto breakDay = TrainingDto.builder()
                .id(String.valueOf(randomUUID()))
                .trainingDifficultyDto(C2)
                .trainingTypeDto(BREAK)
                .description("Dzisiaj dzień wolny :D Weź gorący prysznic i wypoczywaj")
                .build();

        putTrainingsToRepo(repo, firstTrainingA1, secondTrainingA1, firstTrainingA2, secondTrainingA2, firstTrainingB1, secondTrainingB1);

        putTrainingsToRepo(repo, firstTrainingB2, secondTrainingB2, firstTrainingC1, secondTrainingC1, firstTrainingC2, secondTrainingC2);

        repo.put(breakDay.getId(), breakDay);

        return Collections.unmodifiableMap(repo);
    }

    private void putTrainingsToRepo(Map<String, TrainingDto> repo, TrainingDto firstTrainingX1,
                                    TrainingDto secondTrainingX1, TrainingDto firstTrainingY2, TrainingDto secondTrainingY2,
                                    TrainingDto firstTrainingZ1, TrainingDto secondTrainingZ1) {
        repo.put(firstTrainingX1.getId(), firstTrainingX1);
        repo.put(secondTrainingX1.getId(), secondTrainingX1);

        repo.put(firstTrainingY2.getId(), firstTrainingY2);
        repo.put(secondTrainingY2.getId(), secondTrainingY2);

        repo.put(firstTrainingZ1.getId(), firstTrainingZ1);
        repo.put(secondTrainingZ1.getId(), secondTrainingZ1);
    }

    @Override
    public TrainingDto getRandomTrainingByDifficultyLevel(DifficultyLevelDto difficultyLevelDto) {
        return trainingsRepo.values()
                .stream()
                .filter(trainingDto -> trainingDto.getTrainingDifficultyDto() == difficultyLevelDto)
                .findAny()
                .orElseThrow(() -> new PlanException("Ups, nie można znaleźć takiego treningu"));
    }

    @Override
    public TrainingDto getBreakDay() {
        return trainingsRepo.values()
                .stream()
                .filter(trainingDto -> trainingDto.getTrainingTypeDto() == BREAK)
                .findAny()
                .orElseThrow(() -> new PlanException("Ups, nie dnia wolnego"));
    }

    @Override
    public void savePlan(PlanDto planDto) {
        PlanDto planDtoToSave = PlanDto.builder()
                .userId(planDto.getUserId())
                .userLevel(planDto.getUserLevel())
                .monday(planDto.getMonday())
                .tuesday(planDto.getTuesday())
                .wednesday(planDto.getWednesday())
                .thursday(planDto.getThursday())
                .friday(planDto.getFriday())
                .saturday(planDto.getSaturday())
                .sunday(planDto.getSunday())
                .id(String.valueOf(randomUUID()))
                .build();
        plansRepo.put(planDtoToSave.getId(), planDtoToSave);
    }

    @Override
    public void updateUserPlan(PlanDto planDto) {
        plansRepo.replace(planDto.getId(), planDto);
    }

    @Override
    public PlanDto getPlanByUserId(String userId) {
        return plansRepo.values()
                .stream()
                .filter(planDto -> planDto.getUserId().equals(userId))
                .findAny()
                .orElseThrow(() -> new CannotFindPlanException("Nie można znaleźć planu dla tego użytkownika"));
    }
}
