package study.issue_mate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.issue_mate.common.ApiResponse;
import study.issue_mate.common.SuccessType;
import study.issue_mate.dto.IssueCreateRequestDto;
import study.issue_mate.dto.IssueFilterRequestDto;
import study.issue_mate.dto.IssueListResponseDto;
import study.issue_mate.service.IssueService;

import java.util.List;

@RequestMapping("/api/issue")
@RestController
@RequiredArgsConstructor
public class IssueController {
    private final IssueService issueService;

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<List<IssueListResponseDto>>> getIssueList(@AuthenticationPrincipal String userEmail,
                                                                          IssueFilterRequestDto issuefilterRequestDto) {
        List<IssueListResponseDto> issueList = issueService.getIssueList(issuefilterRequestDto);

        return ResponseEntity.ok(ApiResponse.success(SuccessType.INQUERY_SUCCESS, issueList));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<?>> getIssueList(@AuthenticationPrincipal String userEmail,
                                                      IssueCreateRequestDto issueCreateRequestDto) {
        // 생성시 입력값 - projectId, issueType, assignee, summary, parentFields(parentIssueId)
        issueService.createIssue(issueCreateRequestDto);

        return ResponseEntity.ok(ApiResponse.success(SuccessType.INQUERY_SUCCESS));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<?>> deleteIssueList() {
        // 삭제시 입력값 - issueKey
        issueService.deleteIssue();
    }
}
