package study.issue_mate.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessType {
    INQUERY_SUCCESS(HttpStatus.OK,"조회에 성공하였습니다."),
    CREATE_SUCCESS(HttpStatus.OK,"등록에 성공하였습니다.");

    private final HttpStatus status;
    private final String desc;
}
