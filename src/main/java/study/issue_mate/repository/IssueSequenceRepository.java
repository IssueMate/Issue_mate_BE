package study.issue_mate.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.issue_mate.entity.IssueSequence;
import study.issue_mate.entity.Project;

import java.util.Optional;

public interface IssueSequenceRepository extends JpaRepository<IssueSequence, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM IssueSequence s WHERE s.projectId = :projectId")
    Optional<IssueSequence> findWithLock(@Param("projectId") Long projectId);
}
