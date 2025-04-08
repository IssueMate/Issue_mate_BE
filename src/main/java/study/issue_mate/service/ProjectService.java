package study.issue_mate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.issue_mate.dto.RequestCreateProjectDto;
import study.issue_mate.entity.Project;
import study.issue_mate.repository.ProjectRepository;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    public void save(RequestCreateProjectDto requestCreateProjectDto) {

        Project project = Project.builder()
                .projectName(requestCreateProjectDto.getProjectName())
                .projectKey(Project.generateProjectKey(requestCreateProjectDto.getProjectName()))
                .description(requestCreateProjectDto.getDescription())
                .build();

        projectRepository.save(project);
    }
}
