package study.issue_mate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestVerifyCodeDto {
    private String name;
    private String phoneNumber;
    private String code;

}
