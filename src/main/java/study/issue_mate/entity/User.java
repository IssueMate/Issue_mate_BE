package study.issue_mate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import study.issue_mate.entity.base.BaseTimeEntity;

@Entity
@Getter @Setter
public class User extends BaseTimeEntity {
    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String password;
    private String username;
    private String userEmail;
    private String phone;

    // 사용자 계정 상태 ( ex : 휴면, 활성화 등 ) 추후 적용
//    private Status status;
    private String profile;

}
