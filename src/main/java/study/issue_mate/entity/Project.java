package study.issue_mate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import study.issue_mate.entity.base.BaseEntity;

@Entity
public class Project extends BaseEntity {
    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String key;
    @Column(nullable = false)
    private String title;
    private String description;

//    private LocalDate startDate;
//    private LocalDate endDate;

    // 추후 적용
    //private Status status;

    //private User owner;


}
