package study.issue_mate.common.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut("execution(* *.*.controller..*.*(..))")
    private void controllerLogging(){}

//    @Pointcut("execution(* *..controller..*(..)) || execution(* *..service..*(..)) || execution(* *..repository..*(..))")
    @Pointcut("execution(* *.*.service..*(..))")
    public void serviceExceptionLogging() {}

    @Around("controllerLogging()")
    public Object aroundLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        final String method = request.getMethod();
        final String requestURI = request.getRequestURI();
        final Object[] args = proceedingJoinPoint.getArgs();

        log.info("{} {} args={}", method, requestURI, args);

        return proceedingJoinPoint.proceed();
    }

    @AfterThrowing(pointcut = "serviceExceptionLogging()", throwing = "throwable")
    public void afterThrowingServiceException(JoinPoint joinPoint, Throwable throwable) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String className = method.getDeclaringClass().getSimpleName();
        String methodName = method.getName();
        Object[] args = joinPoint.getArgs();

//        log.error("Error in service - Class: {}, Method: {}, Args: {}, Exception: {}, Message: {}",
//                className, methodName, args, throwable.getClass().getName(), throwable.getMessage(), throwable);

        log.error("======================= Error in service =======================");
        log.error("ClassName : {}",className);
        log.error("Method: {}",methodName);
        log.error("Args: {}", args);
        log.error("Exception: {}",throwable.getClass().getName());
        log.error("Message: {}",throwable.getMessage());
//        log.error(throwable);
        log.error("================================================================");
    }

//    @Around("controllerLogging()")
//    public Object aroundLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//        // 메서드 정보 받아오기
//        Method method = getMethod(proceedingJoinPoint);
//        log.info("======= method name = {} =======", method.getName());
//
//        // 파라미터 받아오기
//        Object[] args = proceedingJoinPoint.getArgs();
//        if (args.length == 0) log.info("no parameter");
//        for (Object arg : args) {
//            log.info("parameter type = {}", arg.getClass().getSimpleName());
//            log.info("parameter value = {}", arg);
//        }
//
//        // proceed()를 호출하여 실제 메서드 실행
//        Object returnObj = proceedingJoinPoint.proceed();
//
//        // 메서드의 리턴값 로깅
//        log.info("return type = {}", returnObj.getClass().getSimpleName());
//        log.info("return value = {}", returnObj);
//
//        return returnObj;
//    }
//    private Method getMethod(ProceedingJoinPoint proceedingJoinPoint) {
//        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
//        return signature.getMethod();
//    }
}
