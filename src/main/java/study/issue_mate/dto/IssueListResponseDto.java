package study.issue_mate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.issue_mate.entity.enums.IssueType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IssueListResponseDto {
    //response field
    private String issueKey;
    private IssueType issueType;
    private String description;
//    private Status status;
//    private Priority priority;
    private String assignedTo;
    private LocalDate dueDate;
    // 리스트?
//    private String labels;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String reportedBy;
    // rank --> 추후 정렬 알고리즘 적용 처리(참고 : LexoRank)
//    private
    private String parentIssueKey;
}
