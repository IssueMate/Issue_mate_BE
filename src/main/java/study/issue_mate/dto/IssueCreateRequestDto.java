package study.issue_mate.dto;

import lombok.Getter;
import lombok.Setter;
import study.issue_mate.entity.Issue;
import study.issue_mate.entity.Project;
import study.issue_mate.entity.User;
import study.issue_mate.entity.enums.IssueType;

@Getter
@Setter
public class IssueCreateRequestDto {

    // Project project, IssueType issueType, User assignee, String summary, Issue parentIssueId
    Project project;
    IssueType issueType;
    User assignee;
    String summary;
    Issue parentIssue;
}
