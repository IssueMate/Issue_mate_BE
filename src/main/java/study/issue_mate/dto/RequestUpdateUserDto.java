package study.issue_mate.dto;

import lombok.Getter;

@Getter
public class RequestUpdateUserDto {
    private String password;
    private String name;
    private String phone;
}
