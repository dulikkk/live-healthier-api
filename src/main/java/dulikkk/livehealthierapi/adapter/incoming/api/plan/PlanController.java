package dulikkk.livehealthierapi.adapter.incoming.api.plan;

import dulikkk.livehealthierapi.adapter.incoming.api.ApiEndpoint;
import dulikkk.livehealthierapi.adapter.incoming.api.ApiResponse;
import dulikkk.livehealthierapi.domain.plan.PlanDomainFacade;
import dulikkk.livehealthierapi.domain.plan.dto.PlanDto;
import dulikkk.livehealthierapi.domain.plan.dto.TrainingDto;
import dulikkk.livehealthierapi.domain.plan.dto.command.ChangeTrainingCommand;
import dulikkk.livehealthierapi.domain.plan.dto.exception.CannotFindPlanException;
import dulikkk.livehealthierapi.domain.plan.dto.exception.PlanException;
import dulikkk.livehealthierapi.domain.plan.dto.exception.PlanServerException;
import dulikkk.livehealthierapi.domain.plan.query.PlanQueryRepository;
import dulikkk.livehealthierapi.domain.user.dto.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.DayOfWeek;
import java.time.LocalDate;

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
    ResponseEntity<PlanResponse> getPlanByUserId(@RequestParam("id") String userId) {
        if (userId == null) {
            throw new UserException("Nie podano identyfikatora użytkownika");
        } else {
            PlanDto planDto = planQueryRepository.getPlanByUserId(userId)
                    .orElseThrow(() -> new CannotFindPlanException(
                            "Nie można znaleźć twojego planu :( Spróbuj ponownie później"));

            PlanResponse planResponse = PlanResponse.builder()
                    .availableChangesThiWeek(planDto.getAvailableChangesThiWeek())
                    .monday(planDto.getMonday())
                    .tuesday(planDto.getTuesday())
                    .wednesday(planDto.getWednesday())
                    .thursday(planDto.getThursday())
                    .friday(planDto.getFriday())
                    .saturday(planDto.getSaturday())
                    .sunday(planDto.getSunday())
                    .today(getTodayTraining(planDto))
                    .build();

            return ResponseEntity.ok(planResponse);
        }
    }

    TrainingDto getTodayTraining(PlanDto planDto) {
        LocalDate localDate = LocalDate.now();
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        switch (dayOfWeek) {
            case MONDAY:
                return planDto.getMonday();
            case TUESDAY:
                return planDto.getTuesday();
            case WEDNESDAY:
                return planDto.getWednesday();
            case THURSDAY:
                return planDto.getThursday();
            case FRIDAY:
                return planDto.getFriday();
            case SATURDAY:
                return planDto.getSaturday();
            case SUNDAY:
                return planDto.getSunday();
            default:
                throw new PlanServerException("Nie można znaleźć dzisiejszego treningu");
        }
    }
}
