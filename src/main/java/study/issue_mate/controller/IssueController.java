package study.issue_mate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.issue_mate.common.ApiResponse;
import study.issue_mate.common.SuccessType;
import study.issue_mate.dto.IssueCreateRequestDto;
import study.issue_mate.dto.IssueFilterRequestDto;
import study.issue_mate.dto.IssueResponseDto;
import study.issue_mate.dto.IssueUpdateRequestDto;
import study.issue_mate.service.IssueService;

import java.util.List;

@RequestMapping("/api/issue")
@RestController
@RequiredArgsConstructor
public class IssueController {
    private final IssueService issueService;

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<List<IssueResponseDto>>> getIssueList(@AuthenticationPrincipal String userEmail,
                                                                            IssueFilterRequestDto issuefilterRequestDto) {
        List<IssueResponseDto> issueList = issueService.getIssueList(issuefilterRequestDto);

        return ResponseEntity.ok(ApiResponse.success(SuccessType.INQUERY_SUCCESS, issueList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<IssueResponseDto>> getIssue(@AuthenticationPrincipal String userEmail,
                                                     @PathVariable Long issueId) {
        IssueResponseDto issueResponseDto = issueService.getIssueById(issueId);
        return ResponseEntity.ok(ApiResponse.success(SuccessType.INQUERY_SUCCESS, issueResponseDto));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<?>> getIssueList(@AuthenticationPrincipal String userEmail,
                                                      IssueCreateRequestDto issueCreateRequestDto) {
        // 생성시 입력값 - projectId, issueType, assignee, summary, parentFields(parentIssueId)
        issueService.createIssue(issueCreateRequestDto);

        return ResponseEntity.ok(ApiResponse.success(SuccessType.INQUERY_SUCCESS));
    }

    @DeleteMapping("/{issueKey}")
    public ResponseEntity<?> deleteIssue(@AuthenticationPrincipal String userEmail,
                                         @PathVariable String issueKey) {
        issueService.deleteIssue(issueKey);

        return ResponseEntity.ok().build();
    }

//    @PutMapping
    @PatchMapping("/{issueId}")
    public ResponseEntity<?> updateIssue(@AuthenticationPrincipal String userEmail,
                                         @PathVariable Long issueId,
                                         IssueUpdateRequestDto updateIssueRequestDto) {
        // 삭제시 입력값 - issueKey
        issueService.updateIssue(issueId, updateIssueRequestDto);

        return ResponseEntity.ok().build();
    }
}
