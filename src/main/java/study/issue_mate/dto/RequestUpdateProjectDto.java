package study.issue_mate.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RequestUpdateProjectDto {

    @NotBlank
    private String projectName;

    private String description;

    // 추후 적용
    //private Status status;

//    private User owner;
}
