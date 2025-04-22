package study.issue_mate.service;

import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import study.issue_mate.dto.RequestCreateProjectDto;
import study.issue_mate.dto.RequestUpdateProjectDto;
import study.issue_mate.entity.Project;
import study.issue_mate.entity.User;
import study.issue_mate.exception.CustomException;
import study.issue_mate.exception.ErrorType;
import study.issue_mate.repository.ProjectRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    @Transactional
    public void save(RequestCreateProjectDto requestCreateProjectDto, User owner) {

        log.info("Owner Email: {}", owner.getUserEmail());
        log.info("Project Name: {}", requestCreateProjectDto.getProjectName());

        try {
            Project project = Project.builder()
                .projectName(requestCreateProjectDto.getProjectName())
                .projectKey(Project.generateProjectKey(requestCreateProjectDto.getProjectName()))
                .description(requestCreateProjectDto.getDescription())
                .owner(owner)
                .build();

            projectRepository.save(project);
        } catch (Exception e) {
            log.error("🛑 프로젝트 저장 중 예외 발생", e);
            throw e; // 예외 다시 던져서 컨트롤러 어드바이스에서 처리되도록
        }
    }
    public void update(Long projectId,RequestUpdateProjectDto requestUpdateProjectDto) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(()-> new CustomException(ErrorType.PROJECT_NOT_FOUND));

        project.update(requestUpdateProjectDto.getProjectName(), requestUpdateProjectDto.getDescription());
        projectRepository.save(project);
    }
}
