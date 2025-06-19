package study.issue_mate.dto;


import lombok.Getter;
import study.issue_mate.entity.Issue;

import java.time.LocalDate;

@Getter
public class IssueFilterRequestDto {
    // -- 추가 구현 필터
    // 나에게 할당은 추후 구현
    // 완료 항목
    // -- 기본 필터

    // 프로젝트 키
    private String projectKey;

    // 이슈 키
    private String issueKey;
    // 이슈 유형
//    private IssueType issueType;

    // 키워드
    private String keyword;

    // 이슈 필드에서 startDate 는 추가 설정해 주어야 조건 추가 가능, 따라서 보류
//    // 시작일자
//    private LocalDate startDate;

    // 기한 날짜
    private LocalDate dueDate;
    // 팀(미구현)
//    private Team team;

    // 담당자
    private String assignedTo;

    // 레이블
//    private Label labels;
    // 보낸 사람(보류)
//    private User assignedBy;

    // 보고자
    private String reportedBy;

    // 상위 항목
    private Issue parentIssue;
    // 상태
//    private Status issueStatus;

    // 생성일자
    private LocalDate createdDate;
    // 수정일자
    private LocalDate lastModifiedDate;
    // 우선순위
//    private String priority;

    // 정렬?


    // 추후 정리
    // 사용자의 경우 ID 값을 그대로 사용해서 Long 타입으로 놓는게 맞는지 아니면 이름을 두는게 맞는지?

}
