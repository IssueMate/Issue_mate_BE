package study.issue_mate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import study.issue_mate.entity.base.BaseEntity;

@Entity
@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Project extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "project_id")
    private Long id;

//    @Column(nullable = false, unique = true)
    private String projectKey;

    @Column(nullable = false)
    private String projectName;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

     //추후 추가 예정
     //   private Status status;
    public static String generateProjectKey(String projectName) {
        String[] words;

        if (projectName.contains("_")) {
            words = projectName.split("_");
        } else if (projectName.contains("-")) {
            words = projectName.split("-");
        } else if (projectName.contains(" ")) {
            words = projectName.split("\\s+");
        } else {
            words = splitCamelCase(projectName);
        }

        StringBuilder projectKey = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                projectKey.append(Character.toUpperCase(word.charAt(0)));
            }
        }

        if (projectKey.length() < 2 && words.length > 0 && words[0].length() > 1) {
            projectKey.append(words[0].substring(1, Math.min(words[0].length(), 3)).toUpperCase());
        }

        return projectKey.toString();
    }

    private static String[] splitCamelCase(String projectName) {
        String spaced = projectName.replaceAll("([A-Z])", " $1");
        spaced = spaced.trim();

        return spaced.split("\\s+");
    }

    public void update(String projectName, String description) {
        this.projectName = projectName;
        this.description = description;
        // 프로젝트 키도 이름에 따라 재생성
        this.projectKey = generateProjectKey(projectName);
    }
}
