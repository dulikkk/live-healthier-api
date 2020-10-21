package dulikkk.livehealthierapi.infrastructure.plan.mongoDb.training;

import dulikkk.livehealthierapi.domain.plan.dto.TrainingDto;

public class TrainingConverter {

    public TrainingDocument toDocument(TrainingDto trainingDto){
        return TrainingDocument.builder()
                .id(trainingDto.getId())
                .description(trainingDto.getDescription())
                .trainingDifficulty(trainingDto.getTrainingDifficultyDto())
                .trainingType(trainingDto.getTrainingTypeDto())
                .build();
    }

    public TrainingDto toDto(TrainingDocument trainingDocument){
        return TrainingDto.builder()
                .id(trainingDocument.getId())
                .description(trainingDocument.getDescription())
                .trainingDifficultyDto(trainingDocument.getTrainingDifficulty())
                .trainingTypeDto(trainingDocument.getTrainingType())
                .build();
    }
}
