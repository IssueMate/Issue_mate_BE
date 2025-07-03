package study.issue_mate.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class IssueSequence {

    @Id
    private Long projectId;

    private Long nextVal;

    public IssueSequence(Long projectId, Long startVal) {
        this.projectId = projectId;
        this.nextVal = startVal;
    }

    public void incrementNextVal() {
        nextVal++;
    }
}
