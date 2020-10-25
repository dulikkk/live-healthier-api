package dulikkk.livehealthierapi.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SexDto {
    @JsonProperty("male") MALE,
    @JsonProperty("female") FEMALE,
    @JsonProperty("other") OTHER,
}
