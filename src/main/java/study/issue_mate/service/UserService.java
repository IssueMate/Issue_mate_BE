package study.issue_mate.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Request;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.issue_mate.dto.RequestUpdateUserDto;
import study.issue_mate.dto.ResponseUpdateUserDto;
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
                .name(signUpRequestDto.getName())
                .phone(signUpRequestDto.getPhone())
                .userEmail(signUpRequestDto.getUserEmail())
                .password(encodedPassword)
                .build();

        userRepository.save(signUpUser);
    }
    @Transactional
    public boolean checkEmail(String userEmail){
        return userRepository.existsByUserEmail(userEmail);
    }

    @Transactional
    public void deleteByEmail(String userEmail) {
        User user = userRepository.findByUserEmail(userEmail)
            .orElseThrow(() -> new CustomException(ErrorType.MEMBER_NOT_FOUND));

        userRepository.delete(user);
    }

    @Transactional
    public void updateUser(String userEmail,RequestUpdateUserDto dto) {
        User user = userRepository.findByUserEmail(userEmail)
            .orElseThrow(() -> new CustomException(ErrorType.MEMBER_NOT_FOUND));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(dto.getPassword());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(encodedPassword); // 꼭 암호화
        }

        if (dto.getName() != null) {
            user.setName(dto.getName());
        }

        if (dto.getPhone() != null) {
            user.setPhone(dto.getPhone());
        }
    }

    private void validateDuplicateEmail(String email) {
        if (userRepository.existsByUserEmail(email)) {
            throw new CustomException(ErrorType.ALREADY_EXISTS);
        }
    }
}
