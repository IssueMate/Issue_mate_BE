package study.issue_mate.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import study.issue_mate.common.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum ErrorType implements ErrorCode {
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 프로젝트입니다."),
    ALREADY_EXISTS(HttpStatus.CONFLICT, "사용중인 이메일입니다."),
    TEST_ERROR(HttpStatus.BAD_REQUEST, "테스트 에러");

    private final HttpStatus status;
    private final String desc;
}
