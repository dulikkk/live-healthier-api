package dulikkk.livehealthierapi.adapter.incoming.api.statistics;

import dulikkk.livehealthierapi.adapter.incoming.api.ApiEndpoint;
import dulikkk.livehealthierapi.adapter.incoming.api.ApiResponse;
import dulikkk.livehealthierapi.domain.statistics.StatisticsDomainFacade;
import dulikkk.livehealthierapi.domain.statistics.dto.StatisticsDto;
import dulikkk.livehealthierapi.domain.statistics.dto.exception.CannotFindStatisticsException;
import dulikkk.livehealthierapi.domain.statistics.query.StatisticsQueryRepository;
import dulikkk.livehealthierapi.domain.user.dto.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static java.time.LocalDateTime.now;

@RequiredArgsConstructor
@RestController
public class StatisticsController {

    private final StatisticsDomainFacade statisticsDomainFacade;
    private final StatisticsQueryRepository statisticsQueryRepository;

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

    @GetMapping(ApiEndpoint.GET_USER_STATISTICS)
    ResponseEntity<StatisticsDto> getStatisticsByUserId(@RequestParam("id") String userId) {
        if (userId == null) {
            throw new UserException("Nie podano identyfikatora użytkownika");
        } else {
            StatisticsDto statisticsDto = statisticsQueryRepository.getStatisticsByUserId(userId)
                    .orElseThrow(() -> new CannotFindStatisticsException(
                            "Nie można znaleźć twoich statystyk :( Spróbuj ponownie później"));
            return ResponseEntity.ok(statisticsDto);
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
