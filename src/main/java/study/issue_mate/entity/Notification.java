package study.issue_mate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import study.issue_mate.entity.base.BaseTimeEntity;

@Entity
public class Notification extends BaseTimeEntity {
    @Id @GeneratedValue
    @Column(name = "notification_id")
    private Long id;

    private String message;

//    private String notificationType;
    private boolean isRead = false;
}

