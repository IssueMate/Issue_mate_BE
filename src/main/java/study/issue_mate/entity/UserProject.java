package study.issue_mate.entity;

import jakarta.persistence.*;

@Entity
public class UserProject {
    @Id @GeneratedValue
    @Column(name = "user_project_id")
    private Long userProjectId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
