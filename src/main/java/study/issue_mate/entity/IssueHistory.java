package study.issue_mate.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import study.issue_mate.entity.base.BaseEntity;

import java.time.LocalDateTime;

public class IssueHistory extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "issue_history_id")
    private Long id;

    // 변경된 이슈
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id", nullable = false)
    private Issue issue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChangeType changeType;

    private String oldValue;
    private String newValue;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "changed_by", nullable = false)
//    private User changedBy;

    // 추후 변경 여부 확인
//    @CreatedDate
//    private LocalDateTime changedAt;

    public enum ChangeType {
        STATUS_CHANGE,      // 상태 변경
        ASSIGNEE_CHANGE,    // 담당자 변경
        PRIORITY_CHANGE,    // 우선순위 변경
        DESCRIPTION_UPDATE, // 설명 수정
        COMMENT_ADDED,      // 댓글 추가
        ATTACHMENT_ADDED,   // 첨부파일 추가
        CUSTOM_FIELD_UPDATE // 커스텀 필드 업데이트
    }
}