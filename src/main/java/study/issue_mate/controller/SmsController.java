package study.issue_mate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.issue_mate.service.SmsService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/sms")
public class SmsController {

    private final SmsService smsService;

    // 인증번호 발송
    @PostMapping("/send")
    public ResponseEntity<?> sendAuthCode(@RequestParam String phone) {
        try {
            smsService.sendAuthCode(phone);
            log.info("[SMS] 인증번호 발송 요청 완료 - to: {}", phone);
            return ResponseEntity.ok().body("인증번호가 전송되었습니다.");
        } catch (Exception e) {
            log.error("[SMS] 인증번호 발송 요청 실패 - to: {}, 에러: {}", phone, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("SMS 발송에 실패했습니다: " + e.getMessage());
        }
    }

    // 인증번호 검증
    @PostMapping("/verify")
    public ResponseEntity<?> verifyAuthCode(
        @RequestParam String phone,
        @RequestParam String code
    ) {
        boolean result = false;
        try {
            result = smsService.verifyAuthCode(phone, code);
        } catch (Exception e) {
            log.error("[SMS] 인증번호 검증 중 예외 - to: {}, 에러: {}", phone, e.getMessage());
            return ResponseEntity.internalServerError().body("인증 중 서버 오류: " + e.getMessage());
        }

        if (result) {
            log.info("[SMS] 인증번호 검증 성공 - to: {}", phone);
            return ResponseEntity.ok().body("인증 성공!");
        } else {
            log.warn("[SMS] 인증번호 검증 실패 - to: {}", phone);
            return ResponseEntity.badRequest().body("인증 실패: 인증번호가 일치하지 않습니다.");
        }
    }
}