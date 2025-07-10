package study.issue_mate.service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final RedisTemplate<String, Object> redisTemplate;

    private final Random random = new SecureRandom();

    public void sendVerificationCode(String email) {
        String code = generateCode();
        String subject = "이메일 인증 코드";
        String message = "인증 코드는 다음과 같습니다: " + code;

        // 이메일 발송
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailSender.send(mailMessage);

        // Redis에 저장 (3분 유효)
        redisTemplate.opsForValue().set("verify:" + email, code, Duration.ofMinutes(3));
    }

    public boolean verifyCode(String email, String inputCode) {
        Object saved = redisTemplate.opsForValue().get("verify:" + email);
        return saved != null && saved.toString().equals(inputCode);
    }

    private String generateCode() {
        return String.valueOf(100000 + random.nextInt(900000)); // 6자리 숫자
    }
}
