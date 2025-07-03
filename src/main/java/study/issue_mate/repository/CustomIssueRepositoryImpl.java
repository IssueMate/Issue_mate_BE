package study.issue_mate.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import study.issue_mate.dto.IssueFilterRequestDto;
import study.issue_mate.entity.Issue;

import java.time.LocalDate;
import java.util.List;

import static study.issue_mate.entity.QIssue.issue;
import static study.issue_mate.entity.QProject.project;


@RequiredArgsConstructor
public class CustomIssueRepositoryImpl implements CustomIssueRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    // 페이징 처리 보류
//    public Page<Issue> getIssue(IssueFilterRequest issuefilterRequest, Pageable pageable) {
    public List<Issue> getIssue(IssueFilterRequestDto issuefilterRequestDto) {

        // 기본 조회 쿼리
        // "project = 'IM' AND (hierarchyLevel = 1 OR parent IS EMPTY OR (parent IS NOT EMPTY AND parentProject != 'IM')) AND type in standardIssueTypes() ORDER By priority DESC, Rank DESC"

        // 담당자, 이번주 기한 지정시
        // "project = 'IM' AND (assignee = currentUser() AND duedate <= endOfWeek() AND duedate > startOfWeek()) ORDER By priority ASC, Rank ASC"

        // 키워드 검색
        // "project = 'IM' AND ((summary ~ '관리*' OR description ~ '관리*')) ORDER By priority DESC, Rank DESC"



        //---------------------필터 필드-----------
        //  issueKey
    //    private IssueType issueType;
        //  keyword
    //  private Team team;
    //  private User assignedTo;
    //  private Label labels;
    //  private User assignedBy;
    //  private User reportedBy;
    //  private Issue parentIssue;
    // private Status issueStatus;
    // private String priority;
        // rank ??
        //------------------------------------------

        return jpaQueryFactory.select(issue)
                .from(issue)
                .join(project).on(project.eq(issue.project))
                .where(project.projectKey.eq(issuefilterRequestDto.getProjectKey()),
                        issueKeyEq(issuefilterRequestDto.getIssueKey()),
                        keywordLike(issuefilterRequestDto.getKeyword()),
                        assignedToEq(issuefilterRequestDto.getAssignedTo()),
                        reportedByEq(issuefilterRequestDto.getReportedBy()),
                        keywordLike(issuefilterRequestDto.getKeyword()),
                        dueDateEnd(issuefilterRequestDto.getDueDate()),
                        createdBefore(issuefilterRequestDto.getCreatedDate()),
                        createdAfter(issuefilterRequestDto.getCreatedDate()),
                        updatedBefore(issuefilterRequestDto.getLastModifiedDate()),
                        updatedAfter(issuefilterRequestDto.getLastModifiedDate())
                )
                .fetch();
    }

    private BooleanExpression issueKeyEq(String issueKey) {
        return issueKey != null ? issue.issueKey.eq(issueKey) : null;
    }

    // 키워드로 summary 와 description 함께 확인
    private BooleanExpression keywordLike(String keyword) {

        return StringUtils.hasText(keyword) ? issue.summary.contains(keyword).or(issue.description.contains(keyword)) : null;
    }

    private BooleanExpression assignedToEq(String userEmail) {
        return StringUtils.hasText(userEmail) ? issue.assignee.userEmail.eq(userEmail) : null;
    }

    private BooleanExpression reportedByEq(String userEmail) {
        return StringUtils.hasText(userEmail) ? issue.reporter.userEmail.eq(userEmail) : null;
    }

    // 보류
//    private BooleanExpression dueDateStart(LocalDate date) {
//        return date != null ? issue.dueDate.goe(date) : null;
//    }

    private BooleanExpression dueDateEnd(LocalDate date) {
        return date != null ? issue.dueDate.lt(date.plusDays(1L)) : null;
    }

    private BooleanExpression createdAfter(LocalDate date) {
        return date != null ? issue.createdDate.goe(date.atStartOfDay()) : null;
    }

    private BooleanExpression createdBefore(LocalDate date) {
        return date != null ? issue.createdDate.lt(date.plusDays(1L).atStartOfDay()) : null;
    }

    private BooleanExpression updatedAfter(LocalDate date) {
        return date != null ? issue.lastModifiedDate.goe(date.atStartOfDay()) : null;
    }

    private BooleanExpression updatedBefore(LocalDate date) {
        return date != null ? issue.lastModifiedDate.lt(date.plusDays(1L).atStartOfDay()) : null;
    }
}
