package study.issue_mate.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import study.issue_mate.dto.LoginResponseDto;
import study.issue_mate.exception.JWTErrorType;
import study.issue_mate.util.CookieUtil;
import study.issue_mate.util.RedisUtil;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTProvider jwtProvider;
    private final RedisUtil redisUtil;

    // 추후 어댑터 클래스로 정리?
//    private final AuthRepository authRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("Attempting to authenticate user");
        log.info("Request URL : {}", request.getRequestURL());

//        String userEmail = obtainUsername(request);
//        String password = obtainPassword(request);

        // 넘어온 파라미터로 인증정보 처리
        String userEmail = request.getParameter("userEmail");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userEmail, password);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        log.info("Successfully authenticated user");

        String username = authentication.getName();

//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
//        GrantedAuthority auth = iterator.next();
//
//        String role = auth.getAuthority();

        String access = jwtProvider.generateToken("access", username, 10*60*1000L);
        String refresh = jwtProvider.generateToken("refresh", username, 30*60*1000L);

        // redis 저장
        redisUtil.setValues(username, refresh, 30*60*1000L);

        // 추후 분리 필요
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setUserEmail(username);

        response.addHeader("Authorization", "Bearer " + access);
        response.addCookie(CookieUtil.createCookie("refresh", refresh, 30*60));
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), loginResponseDto);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("Unsuccessful authentication");

        //에러 로그 추가 필요
        JwtExceptionResponseUtil.unAuthentication(response, JWTErrorType.USER_NOT_FOUND);
    }
}
