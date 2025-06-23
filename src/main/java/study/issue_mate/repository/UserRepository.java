package study.issue_mate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.issue_mate.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<User,Long> {

    Optional<User> findByUserEmail(String email);
    boolean existsByUserEmail(String email);
    Optional<User> findByProviderAndProviderId(String provider, String providerId); // 로컬과 소셜로그인 중복방지 메서드
}
