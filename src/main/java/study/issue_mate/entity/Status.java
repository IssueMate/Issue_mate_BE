package study.issue_mate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import study.issue_mate.entity.base.BaseTimeEntity;

@Entity
public class Status extends BaseTimeEntity {
    @Id @GeneratedValue
    @Column(name = "status_id")
    private Long id;

    private String status_type;
    private String status_value;
    private boolean available;
}
