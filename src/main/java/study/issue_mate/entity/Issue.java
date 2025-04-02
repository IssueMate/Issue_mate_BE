package study.issue_mate.entity;

import jakarta.persistence.*;
import study.issue_mate.entity.base.BaseEntity;

import java.util.List;

@Entity
public class Issue extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "issue_id")
    private Long id;

    private String title;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Status status;

//    private  priority;

//    private User assignee;
//    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_issue_id")
    private Issue parentIssue;

    @OneToMany(mappedBy = "parentIssue")
    private List<Issue> childIssues;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;
}
