package study.issue_mate.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import study.issue_mate.dto.IssueCreateRequestDto;
import study.issue_mate.entity.base.BaseEntity;
import study.issue_mate.entity.enums.IssueType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@NoArgsConstructor
public class Issue extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "issue_id")
    private Long id;

    private String issueKey;

    private Long issueNumber;

    private String summary;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Status status;

    private LocalDate dueDate;

//    private  priority; // 우선순위

    // 담당자는 배정되기 전까지 할당되지 않음 처리
    @OneToOne(fetch = FetchType.LAZY)
    private User assignee; // 담당자

    @OneToOne(fetch = FetchType.LAZY)
    private User reporter; // 보고자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_issue_id")
    private Issue parentIssue;

    @OneToMany(mappedBy = "parentIssue")
    private List<Issue> childIssues;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    // 보류
    IssueType issueType;


    // 생성시 입력값 - projectId, issueType, assignee, summary, parentFields(parentIssueId)
    // Project project, IssueType issueType, User assignee, String summary, Issue parentIssueId
    public static Issue createIssue(IssueCreateRequestDto issueCreateRequestDto, Long issueNumber) {
        Issue issue = new Issue();
        issue.project = issueCreateRequestDto.getProject();
        issue.issueType = issueCreateRequestDto.getIssueType();
        issue.assignee = issueCreateRequestDto.getAssignee();
        issue.summary = issueCreateRequestDto.getSummary();
        issue.parentIssue = issueCreateRequestDto.getParentIssue();

        // 생성후 채번된 issueKey 사용
        issue.issueKey = issueCreateRequestDto.getProject().getProjectKey() + "-" + issueNumber;
//        issue.issueKey = generateNextIssueKey();

        return issue;
    }

//    // 프로젝트 ID 채번 후 생성 메소드 필요
//    private static String generateNextIssueKey(){
//        // 이슈 키를 어떤식으로 생성할 것인지?
//        // --> 프로젝트 키 + 번호
//
//        return "";
//    }

}
