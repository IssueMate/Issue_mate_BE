package study.issue_mate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import study.issue_mate.entity.base.BaseTimeEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseTimeEntity {
    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    // 조회 키로 UUID 적용?

    @Column(nullable = false, unique = true)
    private String userEmail;
    private String password;
    private String name;
    private String phone;

    // 사용자 계정 상태 ( ex : 휴면, 활성화 등 ) 추후 적용
//    private Status status;
    private String profile;

}
