package study.issue_mate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.issue_mate.service.TestService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final TestService testService;

    @GetMapping("/get")
    public String get() {
        log.trace("trace message");
        log.debug("debug message");
        log.info("info message"); // default
        log.warn("warn message");
        log.error("error message");

        return testService.exceptionTest();
    }

    @PostMapping("/post")
    public String post() {
        log.trace("trace message");
        log.debug("debug message");
        log.info("info message"); // default
        log.warn("warn message");
        log.error("error message");
        testService.exceptionTest();
        return "Hello World";
    }
}
