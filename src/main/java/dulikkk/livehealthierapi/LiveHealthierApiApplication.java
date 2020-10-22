package dulikkk.livehealthierapi;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import dulikkk.livehealthierapi.domain.plan.dto.TrainingDto;
import dulikkk.livehealthierapi.infrastructure.plan.mongoDb.training.TrainingDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static dulikkk.livehealthierapi.domain.plan.dto.DifficultyLevelDto.*;
import static dulikkk.livehealthierapi.domain.plan.dto.DifficultyLevelDto.C2;
import static dulikkk.livehealthierapi.domain.plan.dto.TrainingTypeDto.*;
import static java.util.UUID.randomUUID;

@SpringBootApplication
@EnableScheduling
@EnableEncryptableProperties
public class LiveHealthierApiApplication {


    public static void main(String[] args) {
        SpringApplication.run(LiveHealthierApiApplication.class, args);
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void init() {
//        TrainingDocument firstTrainingA1 = TrainingDocument.builder()
//                .id(String.valueOf(randomUUID()))
//                .trainingDifficulty(A1)
//                .trainingType(INDOOR_EXERCISES)
//                .description("Ćwiczenia w domu. Wykonaj dzisiaj taki set: *Przykładowy set A1*")
//                .build();
//
//        TrainingDocument secondTrainingA1 = TrainingDocument.builder()
//                .id(String.valueOf(randomUUID()))
//                .trainingDifficulty(A1)
//                .trainingType(RUNNING)
//                .description("Przebiegnij dzisiaj 5km tak szybko jak możesz")
//                .build();
//
//        TrainingDocument firstTrainingA2 = TrainingDocument.builder()
//                .id(String.valueOf(randomUUID()))
//                .trainingDifficulty(A2)
//                .trainingType(INDOOR_EXERCISES)
//                .description("Ćwiczenia w domu. Wykonaj dzisiaj taki set: *Przykładowy set A2*")
//                .build();
//
//        TrainingDocument secondTrainingA2 = TrainingDocument.builder()
//                .id(String.valueOf(randomUUID()))
//                .trainingDifficulty(A2)
//                .trainingType(RUNNING)
//                .description("Przebiegnij dzisiaj 3km tak szybko jak możesz")
//                .build();
//
//        TrainingDocument firstTrainingB1 = TrainingDocument.builder()
//                .id(String.valueOf(randomUUID()))
//                .trainingDifficulty(B1)
//                .trainingType(STRETCHING)
//                .description("Rozciąganie. Wykonaj dzisiaj taki set rozciągania: *Przykładowy set rozciągania B1*")
//                .build();
//
//        TrainingDocument secondTrainingB1 = TrainingDocument.builder()
//                .id(String.valueOf(randomUUID()))
//                .trainingDifficulty(B1)
//                .trainingType(RUNNING)
//                .description("Przebiegnij dzisiaj 1.5km tak szybko jak możesz")
//                .build();
//
//        TrainingDocument firstTrainingB2 = TrainingDocument.builder()
//                .id(String.valueOf(randomUUID()))
//                .trainingDifficulty(B2)
//                .trainingType(WALKING)
//                .description("Pospaceruj dzisiaj przez 1.5h")
//                .build();
//
//        TrainingDocument secondTrainingB2 = TrainingDocument.builder()
//                .id(String.valueOf(randomUUID()))
//                .trainingDifficulty(B2)
//                .trainingType(OUTDOOR_EXERCISES)
//                .description("Ćwiczenia na dworze. Wykonaj dzisiaj taki set: *Przykładowy set B2*")
//                .build();
//
//        TrainingDocument firstTrainingC1 = TrainingDocument.builder()
//                .id(String.valueOf(randomUUID()))
//                .trainingDifficulty(C1)
//                .trainingType(WALKING)
//                .description("Pospaceruj dzisiaj przez 1h")
//                .build();
//
//        TrainingDocument secondTrainingC1 = TrainingDocument.builder()
//                .id(String.valueOf(randomUUID()))
//                .trainingDifficulty(C1)
//                .trainingType(INDOOR_EXERCISES)
//                .description("Ćwiczenia w domu. Wykonaj dzisiaj taki set: *Przykładowy set C1*")
//                .build();
//
//        TrainingDocument firstTrainingC2 = TrainingDocument.builder()
//                .id(String.valueOf(randomUUID()))
//                .trainingDifficulty(C2)
//                .trainingType(WALKING)
//                .description("Pospaceruj dzisiaj przez 45 minut")
//                .build();
//
//        TrainingDocument secondTrainingC2 = TrainingDocument.builder()
//                .id(String.valueOf(randomUUID()))
//                .trainingDifficulty(C2)
//                .trainingType(INDOOR_EXERCISES)
//                .description("Ćwiczenia w domu. Wykonaj dzisiaj taki set: *Przykładowy set C2*")
//                .build();
//
//        TrainingDocument breakDay = TrainingDocument.builder()
//                .id(String.valueOf(randomUUID()))
//                .trainingDifficulty(C2)
//                .trainingType(BREAK)
//                .description("Dzisiaj dzień wolny :D Weź gorący prysznic i wypoczywaj")
//                .build();
//
//
//        mongoTemplate.insert(firstTrainingA1);
//        mongoTemplate.insert(secondTrainingA1);
//
//        mongoTemplate.insert(firstTrainingA2);
//        mongoTemplate.insert(secondTrainingA2);
//
//        mongoTemplate.insert(firstTrainingB1);
//        mongoTemplate.insert(secondTrainingB1);
//
//        mongoTemplate.insert(firstTrainingB2);
//        mongoTemplate.insert(secondTrainingB2);
//
//        mongoTemplate.insert(firstTrainingC1);
//        mongoTemplate.insert(secondTrainingC1);
//
//        mongoTemplate.insert(firstTrainingC2);
//        mongoTemplate.insert(secondTrainingC2);
//
//    }

}
