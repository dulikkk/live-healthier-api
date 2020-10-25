package dulikkk.livehealthierapi.domain.plan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TrainingTypeDto {
    @JsonProperty("dzień wolny") BREAK,
    @JsonProperty("spacer") WALKING,
    @JsonProperty("bieganie") RUNNING,
    @JsonProperty("rozciąganie") STRETCHING,
    @JsonProperty("ćwiczenia na dworze") OUTDOOR_EXERCISES,
    @JsonProperty("ćwiczenia w domu") INDOOR_EXERCISES
}
