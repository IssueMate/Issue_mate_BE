package study.issue_mate.util;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    public static Cookie createCookie(String key, String value, int maxAge) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(true);
        return cookie;
    }

    public static Cookie deleteCookie(String key) {
        Cookie cookie = new Cookie(key, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }
}
