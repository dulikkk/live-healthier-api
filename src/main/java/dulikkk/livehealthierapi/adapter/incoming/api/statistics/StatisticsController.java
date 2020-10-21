package dulikkk.livehealthierapi.adapter.incoming.api.statistics;

import dulikkk.livehealthierapi.adapter.incoming.api.ApiEndpoint;
import dulikkk.livehealthierapi.adapter.incoming.api.ApiResponse;
import dulikkk.livehealthierapi.domain.statistics.StatisticsDomainFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static java.time.LocalDateTime.now;

@RequiredArgsConstructor
@RestController
public class StatisticsController {

    private final StatisticsDomainFacade statisticsDomainFacade;

    @PatchMapping(ApiEndpoint.DONE_TRAINING)
    ResponseEntity<ApiResponse> doneTraining(@RequestParam("id") String userId) {
        if (userId == null) {
            return returnDidntPassUserId();
        } else {
             statisticsDomainFacade.doneTraining(userId);
            return returnOk();
        }
    }

    @PatchMapping(ApiEndpoint.DONE_SUPER_CHALLENGE)
    ResponseEntity<ApiResponse> doneSuperChallenge(@RequestParam("id") String userId) {
        if (userId == null) {
            return returnDidntPassUserId();
        } else {
            statisticsDomainFacade.doneSuperChallenge(userId);
            return returnOk();
        }
    }

    private ResponseEntity<ApiResponse> returnDidntPassUserId() {
        ApiResponse apiResponseBadRequest = ApiResponse.builder()
                .content("Nie podano identyfikatora użytkownika")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(now())
                .build();
        return ResponseEntity.badRequest().body(apiResponseBadRequest);
    }

    private ResponseEntity<ApiResponse> returnOk() {
        ApiResponse apiResponseOk = ApiResponse.builder()
                .content("Zrobione :)")
                .status(HttpStatus.OK.value())
                .timestamp(now())
                .build();
        return ResponseEntity.ok(apiResponseOk);
    }
}
