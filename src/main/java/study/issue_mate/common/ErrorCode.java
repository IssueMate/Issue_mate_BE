package study.issue_mate.common;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String name();
    String getDesc();
    HttpStatus getStatus();
}