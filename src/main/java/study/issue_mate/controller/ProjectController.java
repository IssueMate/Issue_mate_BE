package study.issue_mate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.issue_mate.common.ApiResponse;
import study.issue_mate.common.SuccessType;
import study.issue_mate.dto.RequestCreateProjectDto;
import study.issue_mate.dto.RequestUpdateProjectDto;
import study.issue_mate.dto.SignUpRequestDto;
import study.issue_mate.jwt.CustomUserDetails;
import study.issue_mate.service.ProjectService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/project")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/create")
    public ResponseEntity<?> createProject(@Valid @RequestBody RequestCreateProjectDto request,
        @AuthenticationPrincipal CustomUserDetails userDetails){

        if (userDetails == null) {
            log.error("UserDetails is null! 인증이 안 된 상태입니다.");
            throw new RuntimeException("User is not authenticated");
        }
        log.info("Logged in user: {}", userDetails.getUsername());

        projectService.save(request,userDetails.getUser());

        return ResponseEntity.ok(ApiResponse.success(SuccessType.CREATE_SUCCESS));
    }
    @PutMapping("/{projectId}")
    public ResponseEntity<?> updateProject(@PathVariable Long projectId, @RequestBody RequestUpdateProjectDto request){

        projectService.update(projectId,request);

        return ResponseEntity.ok(ApiResponse.success(SuccessType.UPDATE_SUCCESS));
    }
    //내 프로젝트 찾기
//    GetMapping

}
