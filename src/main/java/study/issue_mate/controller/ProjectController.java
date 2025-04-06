package study.issue_mate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.issue_mate.common.ApiResponse;
import study.issue_mate.common.SuccessType;
import study.issue_mate.dto.RequestCreateProjectDto;
import study.issue_mate.dto.SignUpRequestDto;
import study.issue_mate.service.ProjectService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/project")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/create")
    public ResponseEntity<?> createProject(@Valid @RequestBody RequestCreateProjectDto request){

        projectService.save(request);

        return ResponseEntity.ok(ApiResponse.success(SuccessType.INQUERY_SUCCESS));
    }
}
