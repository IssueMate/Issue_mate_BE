package study.issue_mate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import study.issue_mate.common.ApiResponse;
import study.issue_mate.common.SuccessType;
import study.issue_mate.exception.CustomException;
import study.issue_mate.exception.ErrorType;
import study.issue_mate.service.EmailService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestParam String email) {
        emailService.sendVerificationCode(email);
        return ResponseEntity.ok(ApiResponse.success(SuccessType.INQUERY_SUCCESS));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam String email, @RequestParam String code) {
        boolean result = emailService.verifyCode(email, code);

        if (result) {
            return ResponseEntity.ok(ApiResponse.success(SuccessType.VERIFY_SUCCESS));
        } else {
            throw new CustomException(ErrorType.INVALID_EMAIL_CODE);
        }
    }
}
