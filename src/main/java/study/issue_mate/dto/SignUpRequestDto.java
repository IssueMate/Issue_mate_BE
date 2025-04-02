package study.issue_mate.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SignUpRequestDto {
    private String username;
    private String password;
    private String userEmail;
    private String phone;
}
