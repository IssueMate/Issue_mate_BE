package study.issue_mate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.issue_mate.entity.Issue;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long>, CustomIssueRepository {
    void deleteByIssueKey(String issueKey);

    List<Issue> findByIssueKey(String issueKey);
}
