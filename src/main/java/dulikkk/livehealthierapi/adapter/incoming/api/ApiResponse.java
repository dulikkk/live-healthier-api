package dulikkk.livehealthierapi.adapter.incoming.api;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Builder
@Value
public class ApiResponse {

    Object content;

    int status;

    LocalDateTime timestamp;
}
