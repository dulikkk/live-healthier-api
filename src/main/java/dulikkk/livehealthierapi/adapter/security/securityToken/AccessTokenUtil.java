package dulikkk.livehealthierapi.adapter.security.securityToken;

import dulikkk.livehealthierapi.adapter.security.AuthException;
import dulikkk.livehealthierapi.adapter.security.SecurityConstant;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccessTokenUtil {

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String generateAccessToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstant.ACCESS_TOKEN_EXPIRATION_TIME_IN_MILLISECONDS.getNumber()))
                .setAudience(SecurityConstant.TOKEN_AUDIENCE.getConstant())
                .setIssuer(SecurityConstant.TOKEN_ISSUER.getConstant())
                .claim("roles", roles)
                .signWith(SECRET_KEY)
                .compact();
    }

    public Jws<Claims> parseAccessToken(String accessToken) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(accessToken);
    }

    public void validateAccessToken(String accessToken) {
        try {
            parseAccessToken(accessToken);
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException |
                IllegalArgumentException e) {
            throw new AuthException("Zły token dostępu");
        }
    }

    public String getUsernameFromAccessToken(Jws<Claims> claims) {
        return claims.getBody()
                .getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> getAuthoritiesFromJwt(Jws<Claims> claims) {
        return claims.getBody().get("roles", ArrayList.class);
    }
}
