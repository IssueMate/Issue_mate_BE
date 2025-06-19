package study.issue_mate.repository;

import study.issue_mate.dto.IssueFilterRequestDto;
import study.issue_mate.entity.Issue;

import java.util.List;

public interface CustomIssueRepository {

//    Page<Issue> getIssue(IssueFilterRequest issuefilterRequest, Pageable pageable);
    List<Issue> getIssue(IssueFilterRequestDto issuefilterRequestDto/*, Pageable pageable*/);
}
