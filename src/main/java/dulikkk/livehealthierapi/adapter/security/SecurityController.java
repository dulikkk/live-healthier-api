package dulikkk.livehealthierapi.adapter.security;

import dulikkk.livehealthierapi.adapter.incoming.api.ApiEndpoint;
import dulikkk.livehealthierapi.adapter.incoming.api.ApiResponse;
import dulikkk.livehealthierapi.adapter.security.securityToken.RefreshTokenUtil;
import dulikkk.livehealthierapi.adapter.security.securityToken.SecurityTokenPair;
import dulikkk.livehealthierapi.domain.user.UserDomainFacade;
import dulikkk.livehealthierapi.domain.user.dto.command.NewUserCommand;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.time.LocalDateTime.now;

@RequiredArgsConstructor
@RestController
class SecurityController {

    private final UserDomainFacade userDomainFacade;
    private final RefreshTokenUtil refreshTokenUtil;

    @PostMapping(ApiEndpoint.SIGN_UP)
    ResponseEntity<ApiResponse> register(@RequestBody NewUserCommand newUserCommand) {
        userDomainFacade.addNewUser(newUserCommand);

        ApiResponse apiResponse = ApiResponse.builder()
                .content("Rejestracja powiodła się pomyślnie! Wysłaliśmy na twojego maila link aktywacyjny :) " +
                        "Jeżeli nie otrzymałeś go w ciągu paru minut skontaktuj się z naszą obsługą")
                .status(HttpStatus.CREATED.value())
                .timestamp(now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping(ApiEndpoint.USER_ACTIVATION)
    ResponseEntity<ApiResponse> verifyActivationToken(@RequestParam String token) {
        userDomainFacade.activateUser(token);

        ApiResponse apiResponse = ApiResponse.builder()
                .content("Twoje konto zostało zaaktywowane pomyślnie :D")
                .status(HttpStatus.OK.value())
                .timestamp(now())
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    @GetMapping(ApiEndpoint.REFRESH_TOKENS)
    void refreshTokens(HttpServletRequest req, HttpServletResponse res) {
        Cookie refreshTokenCookie = WebUtils.getCookie(req, SecurityConstant.REFRESH_TOKEN_NAME.getConstant());

        String refreshToken = null;
        if (refreshTokenCookie != null) {
            refreshToken = refreshTokenCookie.getValue();
        }

        if (StringUtils.isNotEmpty(refreshToken)) {
            SecurityTokenPair tokens = refreshTokenUtil.refreshAccessAndRefreshToken(refreshToken);

            res.addHeader("Set-Cookie", SecurityConstant.REFRESH_TOKEN_NAME.getConstant() + "=" +
                    tokens.getRefreshToken() + "; HttpOnly; SameSite=strict; path=/");

            res.addHeader("Set-Cookie", SecurityConstant.ACCESS_TOKEN_NAME.getConstant() + "=" +
                    tokens.getAccessToken() + "; HttpOnly; SameSite=strict; path=/");
        } else {
            throw new AuthException("Nie podano tokena odświeżającego. Spróbuj ponownie");
        }
    }

    @PostMapping(ApiEndpoint.LOGOUT)
    ResponseEntity logout(HttpServletRequest req) {
        Cookie refreshTokenCookie = WebUtils.getCookie(req, SecurityConstant.REFRESH_TOKEN_NAME.getConstant());
        String refreshToken = null;
        if (refreshTokenCookie != null) {
            refreshToken = refreshTokenCookie.getValue();
        }

        if (StringUtils.isNotEmpty(refreshToken)) {
            refreshTokenUtil.deleteRefreshToken(refreshToken);
            SecurityContextHolder.clearContext();
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("Nie podano tokena odświeżającego. Spróbuj ponownie");
        }


    }
}
