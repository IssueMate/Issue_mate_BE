package study.issue_mate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoSocialSignUpdto {
    private String kakaoId;
    private String email;
    private String name;
    private String phone;
    private boolean termsAgreed;
}
