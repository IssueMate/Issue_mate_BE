package study.issue_mate.entity;

import jakarta.persistence.*;
import study.issue_mate.entity.base.BaseEntity;

@Entity
public class Issue extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "issue_id")
    private Long id;

    private Long parentId;

    private String title;
    private String description;

//    private  priority;
    @ManyToOne(fetch = FetchType.LAZY)
    private Status status;
}
