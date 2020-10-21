package dulikkk.livehealthierapi.adapter.incoming.api.user;

import dulikkk.livehealthierapi.adapter.incoming.api.ApiEndpoint;
import dulikkk.livehealthierapi.adapter.incoming.api.ApiResponse;
import dulikkk.livehealthierapi.domain.user.UserDomainFacade;
import dulikkk.livehealthierapi.domain.user.dto.command.UpdateHeightCommand;
import dulikkk.livehealthierapi.domain.user.dto.command.UpdateWeightCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static java.time.LocalDateTime.now;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserDomainFacade userDomainFacade;

    @PatchMapping(ApiEndpoint.UPDATE_HEIGHT)
    ResponseEntity<ApiResponse> updateHeight(@RequestBody UpdateHeightCommand updateHeightCommand) {
        userDomainFacade.updateHeight(updateHeightCommand);
        ApiResponse apiResponseOk = ApiResponse.builder()
                .content("Zrobione :)")
                .status(HttpStatus.OK.value())
                .timestamp(now())
                .build();
        return ResponseEntity.ok(apiResponseOk);
    }

    @PatchMapping(ApiEndpoint.UPDATE_WEIGHT)
    ResponseEntity<ApiResponse> updateWeight(@RequestBody UpdateWeightCommand updateWeightCommand) {
        userDomainFacade.updateWeight(updateWeightCommand);
        ApiResponse apiResponseOk = ApiResponse.builder()
                .content("Zrobione :)")
                .status(HttpStatus.OK.value())
                .timestamp(now())
                .build();
        return ResponseEntity.ok(apiResponseOk);
    }
}
