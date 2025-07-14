package study.issue_mate.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.issue_mate.entity.User;
import study.issue_mate.exception.CustomException;
import study.issue_mate.exception.ErrorType;
import study.issue_mate.repository.UserRepository;
import study.issue_mate.util.MaskUtil;

@Service
@RequiredArgsConstructor
public class FindUserInfoService {
    private final UserRepository userRepository;
    private final SmsService smsService;

    @Transactional
    public void sendCode(String name, String phoneNumber){
        boolean exists = userRepository.existsByNameAndPhone(name, phoneNumber);
        if (!exists) {
            throw new CustomException(ErrorType.MEMBER_NOT_FOUND);
        }
        smsService.sendAuthCode(phoneNumber);
    }

    @Transactional
    public String verifyCodeAndSendEmail(String name, String phoneNumber, String code){
        if(!smsService.verifyAuthCode(phoneNumber,code)){
            throw new CustomException(ErrorType.CODE_NOT_MATCH);
        }
        User user = userRepository.findByNameAndPhone(name, phoneNumber);

        return MaskUtil.maskEmail(user.getUserEmail());
    }
}
