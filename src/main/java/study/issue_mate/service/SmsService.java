package study.issue_mate.service;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import study.issue_mate.util.SmsUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsService {
    private final DefaultMessageService messageService;
    private final SmsUtil smsUtil;
    private final StringRedisTemplate redisTemplate;

    /*
    * 인증번호 발송 (CoolSMS + Redis)
    */
    public void sendAuthCode(String phoneNumber) {
        String authCode = SmsUtil.generateAuthCode(); // 인증번호 생성
        String messageText = smsUtil.makeAuthMessage(authCode); // 메세지 포맷팅

        Message message = new Message();
        message.setFrom("010-9205-9228");
        message.setTo(phoneNumber);
        message.setText(messageText);

        // 요청 래핑
        SingleMessageSendingRequest request = new SingleMessageSendingRequest(message);

        // CoolSMS 발송 및 로그
        try {
            SingleMessageSentResponse response = messageService.sendOne(request);
            log.info("[SMS] 인증번호 발송 - to: {}, code: {}, response: {}", phoneNumber, authCode, response);
        } catch (Exception e) {
            log.error("[SMS] 인증번호 발송 실패 - to: {}, 에러: {}", phoneNumber, e.getMessage(), e);
            throw new RuntimeException("SMS 발송에 실패했습니다.");
        }

        // Redis에 인증번호 저장 (3분 유효)
        redisTemplate.opsForValue().set("SMS: AUTH" + phoneNumber, authCode, 3, TimeUnit.MINUTES);
        log.info("[SMS] 인증번호 Redis 저장 - key: {}, code: {}", "SMS:AUTH:" + phoneNumber, authCode);
    }

    /*
    * 인증번호 검증
    */
    public boolean verifyAuthCode(String phoneNumber, String inputCode) {
        String redisKey = "SMS:AUTH:" + phoneNumber;
        String storedCode = redisTemplate.opsForValue().get(redisKey);
        log.info("[SMS] 인증번호 검증 요청 - key: {}, 입력값: {}, 저장값: {}", redisKey, inputCode, storedCode);

        if (storedCode != null && storedCode.equals(inputCode)) {
            redisTemplate.delete(redisKey); // 1회 검증 후 삭제
            log.info("[SMS] 인증번호 검증 성공 - key: {}", redisKey);
            return true;
        }
        log.warn("[SMS] 인증번호 검증 실패 - key: {}, 입력값: {}", redisKey, inputCode);
        return false;
    }
}
