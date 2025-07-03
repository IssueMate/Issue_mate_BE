package study.issue_mate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KakaoUserInfoDto {
    private String email;
    private String name;
    private String kakaoId; // 카카오에서 받은 고유 식별자
    private String phone;
}
