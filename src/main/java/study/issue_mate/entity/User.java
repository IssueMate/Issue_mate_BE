package study.issue_mate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import study.issue_mate.entity.base.BaseTimeEntity;

@Entity
@Getter
@Setter
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

    @Column(unique = true)
    private String phone;

    // 소셜로그인 구분 필드
    private String provider; // 소셜 로그인 타입: 'KAKAO', 'LOCAL' 등
    private String providerId; // 소셜에서 제공하는 유니크아이디(카카오 userId 등)

    // 사용자 계정 상태 ( ex : 휴면, 활성화 등 ) 추후 적용
//    private Status status;
    private String profile;

    // 약관 동의
    private boolean termsAgreed;

}
