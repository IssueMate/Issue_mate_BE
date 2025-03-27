package study.issue_mate.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import study.issue_mate.common.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum ErrorType implements ErrorCode {
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다.");

    private final HttpStatus status;
    private final String desc;
}
