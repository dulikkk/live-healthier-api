package dulikkk.livehealthierapi.adapter.incoming.api.plan;

import dulikkk.livehealthierapi.adapter.incoming.api.ApiEndpoint;
import dulikkk.livehealthierapi.adapter.incoming.api.ApiResponse;
import dulikkk.livehealthierapi.domain.plan.PlanDomainFacade;
import dulikkk.livehealthierapi.domain.plan.dto.PlanDto;
import dulikkk.livehealthierapi.domain.plan.dto.TrainingDto;
import dulikkk.livehealthierapi.domain.plan.dto.command.ChangeTrainingCommand;
import dulikkk.livehealthierapi.domain.plan.dto.exception.CannotFindPlanException;
import dulikkk.livehealthierapi.domain.plan.query.PlanQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static java.time.LocalDateTime.now;

@RequiredArgsConstructor
@RestController
public class PlanController {

    private final PlanDomainFacade planDomainFacade;
    private final PlanQueryRepository planQueryRepository;

    @PatchMapping(ApiEndpoint.CHANGE_TRAINING)
    ResponseEntity<ApiResponse> changeTraining(@RequestBody ChangeTrainingCommand changeTrainingCommand) {
        TrainingDto newTraining = planDomainFacade.changeTraining(changeTrainingCommand);
        ApiResponse apiResponseOk = ApiResponse.builder()
                .content(newTraining)
                .status(HttpStatus.OK.value())
                .timestamp(now())
                .build();
        return ResponseEntity.ok(apiResponseOk);
    }

    @GetMapping(ApiEndpoint.GET_USER_PLAN)
    ResponseEntity<PlanDto> getPlanByUserId(@RequestParam("id") String userId) {
        PlanDto planDto = planQueryRepository.getPlanByUserId(userId)
                .orElseThrow(() -> new CannotFindPlanException(
                        "Nie można znaleźć twojego planu :( Spróbuj ponownie później"));
        return ResponseEntity.ok(planDto);
    }
}
