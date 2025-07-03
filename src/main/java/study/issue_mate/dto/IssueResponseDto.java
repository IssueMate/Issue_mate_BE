package study.issue_mate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.issue_mate.entity.Issue;
import study.issue_mate.entity.enums.IssueType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IssueResponseDto {
    //response field
    private String issueKey;
    private IssueType issueType;
    private String summary;
    private String description;
//    private Status status;
//    private Priority priority;
    private UserResponseDto assignee;
    private LocalDate dueDate;
    // 리스트?
//    private String labels;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private UserResponseDto reporter;
    // rank --> 추후 정렬 알고리즘 적용 처리(참고 : LexoRank)
//    private
    private String parentIssueKey;

    public static IssueResponseDto from(Issue issue) {
        IssueResponseDto issueResponseDto = new IssueResponseDto();
        issueResponseDto.issueKey = issue.getIssueKey();
        issueResponseDto.issueType = issue.getIssueType();
        issueResponseDto.summary = issue.getSummary();
        issueResponseDto.description = issue.getDescription();
        issueResponseDto.assignee = UserResponseDto.from(issue.getAssignee());
        issueResponseDto.reporter = UserResponseDto.from(issue.getReporter());
        issueResponseDto.createdDate = issue.getCreatedDate();
        issueResponseDto.lastModifiedDate = issue.getLastModifiedDate();
        issueResponseDto.dueDate = issue.getDueDate();
        issueResponseDto.parentIssueKey = issue.getParentIssue().getIssueKey();

        return issueResponseDto;
    }
}
