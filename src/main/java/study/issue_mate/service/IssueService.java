package study.issue_mate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.issue_mate.dto.IssueCreateRequestDto;
import study.issue_mate.dto.IssueFilterRequestDto;
import study.issue_mate.dto.IssueResponseDto;
import study.issue_mate.dto.IssueUpdateRequestDto;
import study.issue_mate.entity.Issue;
import study.issue_mate.exception.CustomException;
import study.issue_mate.exception.ErrorType;
import study.issue_mate.repository.IssueRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IssueService {
    private final IssueRepository issueRepository;
    private final IssueKeyGenerator issueKeyGenerator;

    public IssueResponseDto getIssueById(Long issueId) {
        return issueRepository.findById(issueId)
                .map(IssueResponseDto::from) // DTO 변환
                .orElseThrow(() -> new CustomException(ErrorType.ISSUE_NOT_FOUNT));
    }

    public List<IssueResponseDto> getIssueList(IssueFilterRequestDto issuefilterRequestDto) {
        return issueRepository.getIssue(issuefilterRequestDto)
                .stream()
                .map(IssueResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void createIssue(IssueCreateRequestDto issueCreateRequestDto) {
        Long issueNumber = issueKeyGenerator.generateNextIssueNumber(issueCreateRequestDto.getProject().getId());

        Issue issue = Issue.createIssue(issueCreateRequestDto, issueNumber);
        issueRepository.save(issue);
        // 이슈 생성 후 생성된 데이터 row를 return?
    }

    @Transactional
    public void updateIssue(Long issueId, IssueUpdateRequestDto issueUpdateRequestDto) {
        Optional<Issue> findIssue = issueRepository.findById(issueId);

        Issue issue = findIssue.orElseThrow(() -> new CustomException(ErrorType.ISSUE_NOT_FOUNT));

        if (issueUpdateRequestDto.getSummary() != null) {
            issue.changeSummary(issueUpdateRequestDto.getSummary());
        }

        if (issueUpdateRequestDto.getDescription() != null) {
            issue.changeDescription(issueUpdateRequestDto.getDescription());
        }
    }

    @Transactional
    public void deleteIssue(String issueKey) {
        issueRepository.deleteByIssueKey(issueKey);
    }
}
