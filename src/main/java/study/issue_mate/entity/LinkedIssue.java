package study.issue_mate.entity;

import jakarta.persistence.*;

@Entity
public class LinkedIssue {
    @Id @GeneratedValue
    @Column(name = "linked_issue_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id")
    private Issue issue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_issue_id", nullable = false)
    private Issue sourceIssue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_issue_id", nullable = false)
    private Issue targetIssue;

//    @Enumerated(EnumType.STRING)
//    private LinkType linkType;
//
//    public enum LinkType {
//
//    }
}
