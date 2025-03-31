package study.issue_mate.entity;

import jakarta.persistence.*;
import study.issue_mate.entity.base.BaseEntity;

@Entity
public class IssueAttachedFile extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "issue_attached_file_id")
    private Long id;

    private String file_origin_name;
    private String file_saved_name;
    private String file_path;

    @ManyToOne(fetch = FetchType.LAZY)
    private Issue issue;

//    private User uploadedBy;
}
