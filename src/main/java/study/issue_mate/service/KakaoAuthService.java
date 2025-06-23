package study.issue_mate.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import study.issue_mate.dto.KakaoUserInfoDto;
import study.issue_mate.entity.User;
import study.issue_mate.jwt.JWTProvider;
import study.issue_mate.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoAuthService {
    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.redirect.uri}")
    private String redirectUri;

    private final UserRepository userRepository;
    private final JWTProvider jwtProvider;

    // 카카오 로그인 함수 (인가코드 -> JWT)
    @Transactional
    public String kakaoLogin(String code) {
        log.info("[KakaoService] code 일부: {}", code.substring(0, Math.min(10, code.length())));
        // (1) code로 카카오 access token 얻기
        String accessToken = getKakaoAccessToken(code);
        // (2) access token으로 사용자 정보 가져오기
        KakaoUserInfoDto userInfo = getKakaoUserInfo(accessToken);

        // (3) provider/providerId(카카오의 고유id)로 기존 유저 조회
        User user = userRepository.findByProviderAndProviderId("KAKAO", userInfo.getKakaoId()).orElse(null);

        if (user == null) {
            // (3-1) 신규 회원이면 회원 가입 페이지로 이동하도록 예외 던지거나 redirect URL로 반환
            user = createUserFromKakao(userInfo);
            userRepository.save(user);
        }

        // (4) JWT 토큰 발급
        String jwt = jwtProvider.generateToken("access", user.getUserEmail(), 360000L);
        log.info("[KakaoService] JWT 토큰 발급 (일부): {}", jwt.substring(0, 10));
        return jwt;
    }

    // 1. 카카오 인가코드로 액세스 토큰 받는 메서드
    private String getKakaoAccessToken(String code) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token"; // 카카오 토큰 발급 URL
        RestTemplate restTemplate = new RestTemplate(); // Spring 에서 HTTP요청을 보내기 위한 RestTemplate 생성

        // 요청 헤더: content-type 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        //  요청 파라미터 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId); // 카카오 REST API Key
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        // POST 요청 본문 구성 (헤더 + 파라미터)
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        log.info("[KakaoService] 토큰 요청 파라미터: {}", params);

        // 카카오로 POST 요청 보내서 토큰 응답 받기
        ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, request, String.class);

        log.debug("[KakaoService] 토큰 응답 원본: {}", response.getBody());

        ObjectMapper objectMapper = new ObjectMapper(); // JSON 파싱

        try {
            // 응답 body(JSON)를 트리형 객체(JsonNode)로 변환
            JsonNode root = objectMapper.readTree(response.getBody());
            // access_token 키의 값을 String으로 꺼냄
            String accessToken = root.get("access_token").asText();
            log.info("[KakaoService] access_token 일부: {}", accessToken.substring(0, 10));
            return accessToken;
        } catch (Exception e) {
            log.error("[KakaoService] 토큰 파싱 실패", e);
            throw new RuntimeException("카카오 access token 파싱 실패", e);
        }
    }

    // 2. access token으로 카카오 사용자 정보 가져오는 메서드
    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me"; // 사용자 정보 API
        RestTemplate restTemplate = new RestTemplate();

        // 인증 헤더: Bearer 토큰
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<?> request = new HttpEntity<>(headers);

        log.info("[KakaoService] 사용자 정보 요청 (accessToken 일부): {}", accessToken.substring(0, 10));

        // GET 요청으로 사용자 정보 받아오기
        ResponseEntity<String> response = restTemplate.exchange(
            userInfoUrl, HttpMethod.GET, request, String.class);

        log.info("[KakaoService] 사용자 정보 응답: {}", response.getBody());

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // JSON 파싱 (트리형태로 접근)
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode kakaoAccount = root.get("kakao_account");
            String kakaoId = root.get("id").asText();
            String email = kakaoAccount.get("email").asText();
            String nickname = kakaoAccount.get("profile").get("nickname").asText();

            log.info("[KakaoService] 사용자 정보 파싱 결과: email={}, nickname={}", email, nickname);

            return new KakaoUserInfoDto(email, nickname, kakaoId);
        } catch (Exception e) {
            log.error("[KakaoService] 사용자 정보 파싱 실패", e);
            throw new RuntimeException("카카오 사용자 정보 파싱 실패", e);
        }
    }

    // 3. 카카오 정보 -> User 엔티티로 변환 (최소값만)
    private User createUserFromKakao(KakaoUserInfoDto userInfo) {
        User user = new User();
        user.setUserEmail(userInfo.getEmail());
        user.setName(userInfo.getNickname());
        user.setProvider("KAKAO");
        user.setProviderId(userInfo.getKakaoId());
        return user;
    }
}
