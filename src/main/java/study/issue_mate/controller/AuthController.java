package study.issue_mate.controller;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import study.issue_mate.dto.KakaoLoginResultDto;
import study.issue_mate.dto.KakaoRegisterResponseDto;
import study.issue_mate.dto.KakaoSocialSignUpdto;
import study.issue_mate.dto.KakaoUserInfoDto;
import study.issue_mate.exception.CustomException;
import study.issue_mate.service.KakaoAuthService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    // yml 값 주임
    @Value("${kakao.client.id}")
    private String kakaoClientId;

    @Value("${kakao.redirect.uri}")
    private String kakaoRedirectUri;

    private final KakaoAuthService kakaoAuthService;

    @GetMapping("/kakao/login")
    public void redirectToKakao(HttpServletResponse response) throws IOException {
        String url = "https://kauth.kakao.com/oauth/authorize"
            + "?client_id=" + kakaoClientId
            + "&redirect_uri=" + kakaoRedirectUri
            + "&response_type=code"
            + "&scope=account_email,profile_nickname";

        // 로그로 실제 URL 확인 (디버깅)
        log.info("[카카오 로그인] 인증 URL 생성:{}", url);

        response.sendRedirect(url);
    }

    @GetMapping("/kakao/callback")
    public void kakaoCallback(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        try {
            KakaoLoginResultDto result = kakaoAuthService.kakaoLoginResult(code);

            if (result.isNewUser()) {
                String encodedKakaoId = URLEncoder.encode(result.getKakaoId(), StandardCharsets.UTF_8);
                String encodedEmail = URLEncoder.encode(result.getEmail(), StandardCharsets.UTF_8);
                String encodedName = URLEncoder.encode(result.getName(), StandardCharsets.UTF_8);

                String redirectUrl = String.format(
                    "http://localhost:3000/landing?signupNeeded=1&kakaoId=%s&email=%s&name=%s",
                    encodedKakaoId, encodedEmail, encodedName
                );
                log.info("[카카오 로그인] 신규회원 -> 회원가입 안내: {}", redirectUrl);
                response.sendRedirect(redirectUrl);
            } else {
                String redirectUrl = "http://localhost:3000/oauth-success?token=" + result.getJwt();
                log.info("[카카오 로그인] 기존회원 -> JWT 토큰 리다이렉트");
                response.sendRedirect(redirectUrl);
            }
        } catch (Exception e) {
            log.error("[카카오 로그인 콜백] 예외: {}", e.getMessage(), e);
            response.sendRedirect("http://localhost:3000/error?msg=" + e.getMessage());
        }
    }

    // 카카오 회원가입: 추가정보 받아서 저장
    @PostMapping("/kakao/register")
    public ResponseEntity<?> kakaoRegister(@RequestBody KakaoSocialSignUpdto signUpdto) {
        try {
            String jwt = kakaoAuthService.kakaoRegister(signUpdto);
            return ResponseEntity.ok().body(
                new KakaoRegisterResponseDto(jwt, "회원가입 성공")
            );
        } catch (CustomException e) {
            log.warn("[카카오 회원가입] 커스텀 에러(휴대폰번호 중복): {}", e.getMessage());
            return ResponseEntity.status(e.getErrorType().getStatus()).body(
                new KakaoRegisterResponseDto(null, e.getErrorType().getDesc())
            );
        } catch (IllegalArgumentException e) {
            log.warn("[카카오 회원가입] 중복 가입 시도: {}", e.getMessage());
            return ResponseEntity.badRequest().body(
                new KakaoRegisterResponseDto(null, e.getMessage())
            );
        } catch (Exception e) {
            log.error("[카카오 회원가입] 서버 오류: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(
                new KakaoRegisterResponseDto(null, "서버 오류가 발생했습니다.")
            );
        }
    }
}
