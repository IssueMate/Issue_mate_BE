package study.issue_mate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.issue_mate.dto.IssueCreateRequestDto;
import study.issue_mate.dto.IssueFilterRequestDto;
import study.issue_mate.dto.IssueListResponseDto;
import study.issue_mate.entity.Issue;
import study.issue_mate.repository.IssueRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IssueService {
    private final IssueRepository issueRepository;
    private final IssueKeyGenerator issueKeyGenerator;

    public List<IssueListResponseDto> getIssueList(IssueFilterRequestDto issuefilterRequestDto) {

        return issueRepository.getIssue(issuefilterRequestDto)
                .stream()
                .map(issue -> new IssueListResponseDto())
                .collect(Collectors.toList());
    }

    public void createIssue(IssueCreateRequestDto issueCreateRequestDto) {
        Long issueNumber = issueKeyGenerator.generateNextIssueNumber(issueCreateRequestDto.getProject().getId());

        Issue issue = Issue.createIssue(issueCreateRequestDto, issueNumber);
        issueRepository.save(issue);
        // 이슈 생성 후 생성된 데이터 row를 return?
    }

    public void updateIssue(Issue issue) {

    }

    public void deleteIssue(Issue issue) {

    }
}
