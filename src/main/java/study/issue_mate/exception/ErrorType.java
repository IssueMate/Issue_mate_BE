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
    ALREADY_EXISTS_EMAIL(HttpStatus.CONFLICT, "사용중인 이메일입니다."),
    ALREADY_EXISTS_PHONE(HttpStatus.CONFLICT, "사용중인 휴대폰번호입니다."),
    TEST_ERROR(HttpStatus.BAD_REQUEST, "테스트 에러"),

    ISSUE_NOT_FOUNT(HttpStatus.NOT_FOUND, "해당 이슈를 찾을 수 없습니다."),

    PASSWORD_NOT_MATCHED(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호 형식이 유효하지 않습니다"),
    INVALID_EMAIL_CODE(HttpStatus.BAD_REQUEST, "이메일 인증코드가 올바르지 않습니다");


    private final HttpStatus status;
    private final String desc;
}
