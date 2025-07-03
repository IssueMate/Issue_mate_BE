package study.issue_mate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.issue_mate.entity.IssueSequence;
import study.issue_mate.repository.IssueSequenceRepository;

@Service
@RequiredArgsConstructor
public class IssueKeyGenerator {

    private final IssueSequenceRepository issueSequenceRepository;

    @Transactional
    public Long generateNextIssueNumber(Long projectId) {
        IssueSequence sequence = issueSequenceRepository.findWithLock(projectId)
                .orElseGet(() -> {
                    IssueSequence newSeq = new IssueSequence(projectId, 1L);
                    issueSequenceRepository.save(newSeq);
                    return newSeq;
                });
        Long currentVal = sequence.getNextVal();
        sequence.incrementNextVal();
        return currentVal;
    }
}