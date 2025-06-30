package study.issue_mate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoRegisterResponseDto {
    private String token;
    private String message;
}
