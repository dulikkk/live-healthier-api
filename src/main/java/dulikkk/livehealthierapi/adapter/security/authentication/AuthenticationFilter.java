package dulikkk.livehealthierapi.adapter.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import dulikkk.livehealthierapi.adapter.incoming.api.ApiEndpoint;
import dulikkk.livehealthierapi.adapter.security.AuthException;
import dulikkk.livehealthierapi.adapter.security.SecurityConstant;
import dulikkk.livehealthierapi.adapter.security.securityToken.AccessTokenUtil;
import dulikkk.livehealthierapi.adapter.security.securityToken.RefreshTokenUtil;
import dulikkk.livehealthierapi.domain.user.dto.UserDto;
import dulikkk.livehealthierapi.domain.user.query.UserQueryRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserQueryRepository userQueryRepository;
    private final RefreshTokenUtil refreshTokenUtil;
    private final AccessTokenUtil accessTokenUtil = new AccessTokenUtil();

    public AuthenticationFilter(AuthenticationManager authenticationManager, UserQueryRepository userQueryRepository, RefreshTokenUtil refreshTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.userQueryRepository = userQueryRepository;
        this.refreshTokenUtil = refreshTokenUtil;
        setFilterProcessesUrl(ApiEndpoint.SIGN_IN);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            AuthenticationRequest authenticationRequest = new ObjectMapper().readValue(request.getInputStream(),
                    AuthenticationRequest.class);

            UsernamePasswordAuthenticationToken authRequest =
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword());

            return authenticationManager.authenticate(authRequest);
        } catch (IOException e) {
            throw new AuthException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        User authUser = (User) auth.getPrincipal();

        List<String> roles = authUser.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toUnmodifiableList());

        String accessToken = accessTokenUtil.generateAccessToken(authUser.getUsername(), roles);
        String refreshToken = refreshTokenUtil.generateRefreshToken(authUser.getUsername());

        res.addHeader("Set-Cookie", SecurityConstant.REFRESH_TOKEN_NAME.getConstant() + "=" + refreshToken + "; HttpOnly; SameSite=strict; path=/");
        res.addHeader("Set-Cookie", SecurityConstant.ACCESS_TOKEN_NAME.getConstant() + "=" +
                accessToken + "; HttpOnly; SameSite=strict; path=/");

        addAuthenticatedUserInfo(authUser.getUsername(), res);

    }

    private void addAuthenticatedUserInfo(String authUsername, HttpServletResponse res) throws IOException {
        UserDto authUser = userQueryRepository.findByUsername(authUsername)
                .orElseThrow(() -> new AuthException("Ojj, coś poszło nie tak. Spróbuj ponownie"));

        AuthenticatedUserInfo authenticatedUserInfo = AuthenticatedUserInfo.builder()
                .id(authUser.getId())
                .username(authUser.getUsername())
                .email(authUser.getEmail())
                .roles(authUser.getRoles())
                .build();

        ObjectMapper jsonMapper = new ObjectMapper();

        String authUserJson = jsonMapper.writeValueAsString(authenticatedUserInfo);

        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        out.print(authUserJson);
        out.flush();
    }
}
