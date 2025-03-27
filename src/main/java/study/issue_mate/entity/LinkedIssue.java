package study.issue_mate.entity;

import jakarta.persistence.*;

@Entity
public class LinkedIssue {
    @Id @GeneratedValue
    @Column(name = "linked_issue_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "issue_id")
    private Issue issue;

    @ManyToOne
    @JoinColumn(name = "source_issue_id", nullable = false)
    private Issue sourceIssue;

    @ManyToOne
    @JoinColumn(name = "target_issue_id", nullable = false)
    private Issue targetIssue;

//    @Enumerated(EnumType.STRING)
//    private LinkType linkType;
//
//    public enum LinkType {
//
//    }
}
