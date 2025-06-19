package study.issue_mate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.issue_mate.entity.Issue;

public interface IssueRepository extends JpaRepository<Issue, Long>, CustomIssueRepository {
}
