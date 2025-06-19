package study.issue_mate.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import study.issue_mate.entity.User;
import study.issue_mate.exception.JWTErrorType;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTProvider jwtProvider;
    private final JwtBlacklistService jwtBlacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        log.info("JWT Filter is processing the request");

        String authorization = request.getHeader("Authorization");

        if(authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = authorization.split(" ")[1];

        // check if the token is blacklisted
        if (jwtBlacklistService.isTokenBlacklisted(accessToken)) {
            JwtExceptionResponseUtil.unAuthentication(response, JWTErrorType.BLACKLISTED_ACCESS_TOKEN);
            return;
        }

        try {
            jwtProvider.isTokenExpired(accessToken);
        } catch (ExpiredJwtException e) {
            JwtExceptionResponseUtil.unAuthentication(response, JWTErrorType.EXPIRED_ACCESS_TOKEN);
            return;
        }

        String category = jwtProvider.getCategory(accessToken);

        if(!"access".equals(category)) {
            JwtExceptionResponseUtil.unAuthentication(response, JWTErrorType.INVALID_TOKEN_ERROR);
            return;
        }


        // 유저 권한 추출 필요.
//        User user = new User();
//        user.setUserEmail(userEmail);
//        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        // 유저 권한 정의 전까지는 String 으로 일단 사용
        String userEmail = jwtProvider.getUsernameFromToken(accessToken);

        // 스프링 시큐리티 인증 토큰 생성
//        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, null);
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(userEmail, null, null);

        // 세션에 사용자 등룍
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }

//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        // 권한없이 접근 가능한 url 에 접근 시, 헤더에 Authorization 이 있는 경우 에러 발생
//        // permitAll url 은 인증 절차 예외
//        String[] excludePath = {"/login", "api/register"};
//
//        String path = request.getRequestURI();
//
//        return Arrays.stream(excludePath).anyMatch(path::startsWith);
//    }
}
