package study.issue_mate.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;
import study.issue_mate.common.ApiResponse;
import study.issue_mate.common.SuccessType;
import study.issue_mate.exception.JWTErrorType;
import study.issue_mate.util.CookieUtil;
import study.issue_mate.util.RedisUtil;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTProvider jwtProvider;
    private final RedisUtil redisUtil;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        this.doFilter((HttpServletRequest)servletRequest, (HttpServletResponse)servletResponse, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String requestURI = request.getRequestURI();
        if (!requestURI.matches("^\\/logout$")) {

            chain.doFilter(request, response);
            return;
        }

        String requestMethod = request.getMethod();
        if(!"POST".equals(requestMethod)) {
            chain.doFilter(request, response);
            return;
        }

        String refresh = null;
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if("refresh".equals(cookie.getName())) {
                    refresh = cookie.getValue();
                }
            }
        }

        if(refresh == null){
            JwtExceptionResponseUtil.unAuthentication(response, JWTErrorType.REFRESH_TOKEN_NONE);
            return;
        }

        try{
            jwtProvider.isTokenExpired(refresh);
        }catch(ExpiredJwtException e){
            JwtExceptionResponseUtil.unAuthentication(response, JWTErrorType.INVALID_REFRESH_TOKEN);
            return;
        }

        String category = jwtProvider.getCategory(refresh);
        if(!"refresh".equals(category)){
            JwtExceptionResponseUtil.unAuthentication(response, JWTErrorType.INVALID_REFRESH_TOKEN);
            return;
        }
        String userEmail = jwtProvider.getUsernameFromToken(refresh);
        boolean isExist = redisUtil.checkExistsValue(userEmail);

        if(!isExist){
            JwtExceptionResponseUtil.unAuthentication(response, JWTErrorType.INVALID_REFRESH_TOKEN);
            return;
        }
        Cookie cookie = CookieUtil.deleteCookie("refresh");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        // 메시지 추후 변경
        new ObjectMapper().writeValue(response.getOutputStream(), ApiResponse.success(SuccessType.INQUERY_SUCCESS));

    }
}
