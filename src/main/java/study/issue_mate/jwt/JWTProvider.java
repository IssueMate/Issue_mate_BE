package study.issue_mate.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JWTProvider {

    private final SecretKey secretKey;

    public JWTProvider(@Value("${spring.jwt.secretkey}") String secretKey) {
        this.secretKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userEmail", String.class);
    }

    public Boolean isTokenExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String getCategory(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseClaimsJws(token).getPayload().get("category", String.class);
    }

    public String generateToken(String category, String userEmail, /*String role,*/Long expiredMs) {
        return Jwts.builder()
                .claim("category", category)
                .claim("userEmail", userEmail)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch(SignatureException | SecurityException | MalformedJwtException e){
            log.debug("잘못된 JWT 서명입니다.",e);
        } catch(IllegalArgumentException e){
            log.debug("JWT 토큰이 잘못되었습니다.",e);
        } catch(ExpiredJwtException e){
            log.debug("만료된 JWT 토큰입니다.",e);
        } catch(UnsupportedJwtException e){
            log.debug("지원되지 않는 JWT 토큰입니다.",e);
        }

        return false;
    }
}
