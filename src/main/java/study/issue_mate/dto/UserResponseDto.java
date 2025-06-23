package study.issue_mate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.issue_mate.entity.User;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private String profile;

    public static UserResponseDto from(User user) {
        if (user == null) return null;
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.id = user.getId();
        userResponseDto.name = user.getName();
        userResponseDto.email = user.getUserEmail();
        userResponseDto.profile = user.getProfile();

        return userResponseDto;
    }
}
