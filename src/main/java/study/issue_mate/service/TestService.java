package study.issue_mate.service;

import org.springframework.stereotype.Service;
import study.issue_mate.exception.CustomException;
import study.issue_mate.exception.ErrorType;

@Service
public class TestService {

    public String test() {
        return "test";
    }

    public String exceptionTest() {
        throw new CustomException(ErrorType.TEST_ERROR);
    }
}
