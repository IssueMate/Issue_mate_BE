package study.issue_mate.controller;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
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
import study.issue_mate.dto.KakaoSocialSignUpdto;
import study.issue_mate.dto.KakaoUserInfoDto;
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
            String jwt = kakaoAuthService.kakaoLogin(code);
            // 실제 서비스에선 회원가입 구분 필요 (신규면 회원가입화면으로, 기존이면 바로 토큰 리턴)
            response.sendRedirect("http://localhost:3000/oauth-success?token=" + jwt);
        } catch (IllegalStateException e) {
            response.sendRedirect("http://localhost:3000/kakao-signup");
        }
    }

    // 카카오 회원가입: 추가정보 받아서 저장
    @PostMapping("/kakao/register")
    public ResponseEntity<String> kakaoRegister(@RequestBody KakaoSocialSignUpdto signUpdto) {
        String jwt = kakaoAuthService.kakaoRegister(signUpdto);
        return ResponseEntity.ok(jwt);
    }
}
