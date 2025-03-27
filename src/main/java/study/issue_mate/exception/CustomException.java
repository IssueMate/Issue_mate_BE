package study.issue_mate.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private final ErrorType errorType;

    public CustomException(ErrorType errorType) {
        super(errorType.getDesc());
        this.errorType = errorType;
    }
}
