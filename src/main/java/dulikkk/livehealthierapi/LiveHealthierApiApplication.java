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

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import static dulikkk.livehealthierapi.domain.plan.dto.DifficultyLevelDto.*;
import static dulikkk.livehealthierapi.domain.plan.dto.DifficultyLevelDto.C2;
import static dulikkk.livehealthierapi.domain.plan.dto.TrainingTypeDto.*;
import static java.util.UUID.randomUUID;

@SpringBootApplication
@EnableScheduling
@EnableEncryptableProperties
public class LiveHealthierApiApplication {

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Warsaw"));
    }

    public static void main(String[] args) {
        SpringApplication.run(LiveHealthierApiApplication.class, args);
    }

}
