package study.issue_mate.dto;

import lombok.Getter;
import lombok.Setter;
import study.issue_mate.entity.Issue;
import study.issue_mate.entity.User;

@Getter
@Setter
public class IssueUpdateRequestDto {
    // ---- 변경 가능 항목
    String summary;
    String description; // 에디터로 작성된 내용 --> 우선 단순 String 으로 처리. 추후 변경 필요.
    // ----
}
