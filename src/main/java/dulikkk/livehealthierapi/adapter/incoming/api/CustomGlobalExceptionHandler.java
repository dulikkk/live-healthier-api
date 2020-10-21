package dulikkk.livehealthierapi.adapter.incoming.api;

import dulikkk.livehealthierapi.adapter.security.AuthException;
import dulikkk.livehealthierapi.domain.plan.dto.exception.CannotFindPlanException;
import dulikkk.livehealthierapi.domain.plan.dto.exception.PlanException;
import dulikkk.livehealthierapi.domain.statistics.dto.exception.CannotFindStatisticsException;
import dulikkk.livehealthierapi.domain.user.dto.exception.CannotFindUserException;
import dulikkk.livehealthierapi.domain.user.dto.exception.CannotSendTokenException;
import dulikkk.livehealthierapi.domain.user.dto.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static java.time.LocalDateTime.now;

@RestControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApiResponse> UserDomainExceptionHandler(UserException e) {
        ApiResponse apiResponse = ApiResponse.builder()
                .content(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiResponse> authExceptionHandler(AuthException e) {
        ApiResponse apiResponse = ApiResponse.builder()
                .content(e.getMessage())
                .status(HttpStatus.UNAUTHORIZED.value())
                .timestamp(now())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
    }

    @ExceptionHandler(CannotFindUserException.class)
    public ResponseEntity<ApiResponse> cannotFindUserExceptionHandler(CannotFindUserException e) {
        ApiResponse apiResponse = ApiResponse.builder()
                .content(e.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @ExceptionHandler(CannotSendTokenException.class)
    public ResponseEntity<ApiResponse> cannotSendTokenHandler(CannotSendTokenException e) {
        ApiResponse apiResponse = ApiResponse.builder()
                .content(e.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(now())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }

    @ExceptionHandler(PlanException.class)
    public ResponseEntity<ApiResponse> planException(PlanException e) {
        ApiResponse apiResponse = ApiResponse.builder()
                .content(e.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(now())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }

    @ExceptionHandler(CannotFindPlanException.class)
    public ResponseEntity<ApiResponse> cannotFindPlanException(CannotFindPlanException e) {
        ApiResponse apiResponse = ApiResponse.builder()
                .content(e.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @ExceptionHandler(CannotFindStatisticsException.class)
    public ResponseEntity<ApiResponse> cannotFindStatisticsException(CannotFindStatisticsException e) {
        ApiResponse apiResponse = ApiResponse.builder()
                .content(e.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }
}
