package dulikkk.livehealthierapi.adapter.security.authorization;

import dulikkk.livehealthierapi.adapter.security.SecurityConstant;
import dulikkk.livehealthierapi.adapter.security.securityToken.AccessTokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private final AccessTokenUtil accessTokenUtil = new AccessTokenUtil();

    public AuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        getAuthentication(httpServletRequest)
                .ifPresent(auth -> SecurityContextHolder.getContext().setAuthentication(auth)
                );

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private Optional<UsernamePasswordAuthenticationToken> getAuthentication(HttpServletRequest req) {
        Cookie accessTokenCookie = WebUtils.getCookie(req, SecurityConstant.ACCESS_TOKEN_NAME.getConstant());

        String accessToken = null;
        if (accessTokenCookie != null) {
            accessToken = accessTokenCookie.getValue();
        }

        if (StringUtils.isNotEmpty(accessToken)) {

            accessTokenUtil.validateAccessToken(accessToken);

            Jws<Claims> parsedToken = accessTokenUtil.parseAccessToken(accessToken);

            String username = accessTokenUtil.getUsernameFromAccessToken(parsedToken);
            List<String> roles = accessTokenUtil.getAuthoritiesFromJwt(parsedToken);

            List<GrantedAuthority> authorities = roles
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toUnmodifiableList());

            if (StringUtils.isNotEmpty(username)) {
                return Optional.of(new UsernamePasswordAuthenticationToken(username, null, authorities));
            }
        }
        return Optional.empty();
    }
}
