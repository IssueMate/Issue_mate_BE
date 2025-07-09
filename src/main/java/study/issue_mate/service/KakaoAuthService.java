package study.issue_mate.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import study.issue_mate.dto.KakaoLoginResultDto;
import study.issue_mate.dto.KakaoSocialSignUpdto;
import study.issue_mate.dto.KakaoUserInfoDto;
import study.issue_mate.entity.User;
import study.issue_mate.jwt.JWTProvider;
import study.issue_mate.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final UserService userService;
    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.redirect.uri}")
    private String redirectUri;

    private final UserRepository userRepository;
    private final JWTProvider jwtProvider;
    private final ObjectMapper objectMapper;
    private final SmsService smsService;

    // 카카오 로그인 함수: 회원 있으면 JWT 리턴, 없으면 예외(회원가입)
    @Transactional(readOnly = true)
    public KakaoLoginResultDto kakaoLoginResult(String code) {
        log.info("[카카오 로그인] code로 accessToken 및 프로필 조회");
        // (1) code로 카카오 access token 얻기
        String accessToken = getKakaoAccessToken(code);
        // (2) access token으로 사용자 정보 가져오기
        KakaoUserInfoDto userInfo = getKakaoUserInfo(accessToken);

        log.info("[카카오 로그인] userInfo: kakaoId={}, email={}, name={}", userInfo.getKakaoId(), userInfo.getEmail(), userInfo.getName());

        // (3) provider/providerId(카카오의 고유id)로 기존 유저 조회
        User user = userRepository.findByProviderAndProviderId("KAKAO", userInfo.getKakaoId()).orElse(null);

        if (user == null) {
            // (3-1) 신규 회원이면 회원 가입 페이지로 이동하도록 예외 던지거나 redirect URL로 반환
         log.info("[카카오 로그인] 신규 회원, 추가정보 입력");
         return new KakaoLoginResultDto(
             true,
             null,
             userInfo.getKakaoId(),
             userInfo.getEmail(),
             userInfo.getName()
         );
        }

        // (4) 회원일 경우 JWT 토큰 발급
        String jwt = jwtProvider.generateToken("access", user.getUserEmail(), 360000L);
        log.info("[카카오 로그인] 기존 회원, JWT 발급 (userEmail={})", user.getUserEmail());
        return new KakaoLoginResultDto(false, jwt, null, null, null);
    }

    // 소셜 회원가입(추가정보 DTO로 가입)
    @Transactional
    public String kakaoRegister(KakaoSocialSignUpdto signUpdto) {
        log.info("[카카오 회원가입] 카카오 회원가입 시도: kakaoId={}, email={}, name={}", signUpdto.getKakaoId(), signUpdto.getEmail(), signUpdto.getName());


        // 휴대폰 번호 중복 체크
        userService.validateDuplicatePhone(signUpdto.getPhone());

        // 카카오 회원 중복 체크
        if (userRepository.findByProviderAndProviderId("KAKAO", signUpdto.getKakaoId()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 카카오 계정입니다.");
        }
        User user = createUserFromKakao(signUpdto);
        userRepository.save(user);

        // 가입 직후 바로 JWT 토큰 발급
        String jwt = jwtProvider.generateToken("access", user.getUserEmail(), 360000L);
        log.info("[KakaoService] 카카오 회원가입 성공: email={}, jwt(일부): {}", user.getUserEmail(), jwt.substring(0, 10));
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

        log.info("[카카오 토큰] 토큰 요청 파라미터: {}", params);

        // 카카오로 POST 요청 보내서 토큰 응답 받기
        ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, request, String.class);

        try {
            // 응답 body(JSON)를 트리형 객체(JsonNode)로 변환
            JsonNode root = objectMapper.readTree(response.getBody());
            // access_token 키의 값을 String으로 꺼냄
            String accessToken = root.get("access_token").asText();
            log.info("[카카오 토큰] access_token 일부: {}", accessToken.substring(0, 10));
            return accessToken;
        } catch (Exception e) {
            log.error("[카카오 토큰] 토큰 파싱 실패", e);
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

        HttpEntity<?> request = new HttpEntity<>(headers);

        // GET 요청으로 사용자 정보 받아오기
        ResponseEntity<String> response = restTemplate.exchange(
            userInfoUrl, HttpMethod.GET, request, String.class);

        log.info("[카카오 사용자정보] 응답: {}", response.getBody());

        try {
            // JSON 파싱 (트리형태로 접근)
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode kakaoAccount = root.get("kakao_account");
            String kakaoId = root.get("id").asText();
            String email = kakaoAccount.get("email").asText();
            String name = kakaoAccount.get("profile").get("nickname").asText();

            if (email == null || name == null) {
                log.error("[카카오 사용자 정보] 필수 프로필 정보 누락. kakaoId={}", kakaoId);
                throw new IllegalArgumentException("카카오 프로필 정보가 누락되었습니다");
            }
            return new KakaoUserInfoDto(email, name, kakaoId, null);
        } catch (Exception e) {
            log.error("[카카오 사용자 정보]파싱 실패", e);
            throw new RuntimeException("카카오 사용자 정보 파싱 실패", e);
        }
    }

    // 3. 카카오 정보 -> User 엔티티로 변환 (최소값만)
    private User createUserFromKakao(KakaoSocialSignUpdto dto) {
        User user = new User();
        user.setUserEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setProvider("KAKAO");
        user.setProviderId(dto.getKakaoId());
        user.setPhone(dto.getPhone());
        user.setTermsAgreed(dto.isTermsAgreed());
        return user;
    }
}
