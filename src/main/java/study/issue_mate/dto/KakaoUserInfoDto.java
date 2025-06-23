package study.issue_mate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoUserInfoDto {
    private String email;
    private String nickname;
    private String kakaoId; // 카카오에서 받은 고유 식별자
}
