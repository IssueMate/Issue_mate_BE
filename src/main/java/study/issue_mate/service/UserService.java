package study.issue_mate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.issue_mate.dto.SignUpRequestDto;
import study.issue_mate.entity.User;
import study.issue_mate.exception.CustomException;
import study.issue_mate.exception.ErrorType;
import study.issue_mate.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void signUp(SignUpRequestDto signUpRequestDto){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(signUpRequestDto.getPassword());

        validateDuplicateEmail(signUpRequestDto.getUserEmail());

        User signUpUser = User.builder()
                .username(signUpRequestDto.getUsername())
                .phone(signUpRequestDto.getPhone())
                .password(encodedPassword)
                .build();

        userRepository.save(signUpUser);
    }

    private void validateDuplicateEmail(String email) {
        if (userRepository.existsByUserEmail(email)) {
            throw new CustomException(ErrorType.ALREADY_EXISTS);
        }
    }
}
