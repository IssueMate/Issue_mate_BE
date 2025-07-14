package study.issue_mate.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestSendCodeDto {
    @NotBlank
    private String name;
    @NotBlank private String phoneNumber;
}
