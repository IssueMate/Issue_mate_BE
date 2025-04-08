package study.issue_mate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.issue_mate.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

}
