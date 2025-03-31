package study.issue_mate.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import study.issue_mate.util.RedisUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtBlacklistService {

   private final RedisUtil redisUtil;
    /**
     *
     * @param token access token 문자열
     * @param expirationMillis 토큰의 남은 유효시간 (밀리초)
     */
    public void addTokenBlacklist(String token, long expirationMillis) {
        redisUtil.setValues(token, "blacklisted", expirationMillis);
    }

    public boolean isTokenBlacklisted(String token) {
        return redisUtil.checkExistsValue(token);
    }
}
