package study.issue_mate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoLoginResultDto {
    private boolean newUser;
    private String jwt;
    private String kakaoId;
    private String email;
    private String name;

}
