//package study.issue_mate.common.aop;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import java.lang.reflect.Method;
//import java.util.HashMap;
//import java.util.Map;
//
//@Aspect
//@Component
//@Slf4j
//public class LogAspect {
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Pointcut("execution(* *.*.controller..*.*(..))")
//    private void controllerLogging() {}
//
//    @Pointcut("execution(* *.*.service..*(..))")
//    public void serviceExceptionLogging() {}
//
////    private void allComponentLogging() {}
//
//    @Around("controllerLogging()")
//    public Object aroundLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
//                .getRequest();
//        final String method = request.getMethod();
//        final String requestURI = request.getRequestURI();
//        final Object[] args = proceedingJoinPoint.getArgs();
//
////        log.info("{} {} args={}", method, requestURI, args);
//        Map<String, Object> logMessage = new HashMap<>();
//        logMessage.put("method", method);
//        logMessage.put("requestURI", requestURI);
//        logMessage.put("args", args);
//
//        // JSON 문자열로 변환
//        String jsonMessage = objectMapper.writeValueAsString(logMessage);
//
//        log.info(jsonMessage);
//
//        return proceedingJoinPoint.proceed();
//    }
//
//    @AfterThrowing(pointcut = "serviceExceptionLogging()", throwing = "throwable")
//    public void afterThrowingServiceException(JoinPoint joinPoint, Throwable throwable) {
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        String className = method.getDeclaringClass().getSimpleName();
//        String methodName = method.getName();
//        Object[] args = joinPoint.getArgs();
//
//        log.error("Error in service - Class: {}, Method: {}, Args: {}, Exception: {}, Message: {}",
//                className, methodName, args, throwable.getClass().getName(), throwable.getMessage());
//
//        if(log.isDebugEnabled()){
//            log.debug("Exception occurred", throwable);
//        }
//    }
//
////    @Around("controllerLogging()")
////    public Object aroundLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
////        // 메서드 정보 받아오기
////        Method method = getMethod(proceedingJoinPoint);
////        log.info("======= method name = {} =======", method.getName());
////
////        // 파라미터 받아오기
////        Object[] args = proceedingJoinPoint.getArgs();
////        if (args.length == 0) log.info("no parameter");
////        for (Object arg : args) {
////            log.info("parameter type = {}", arg.getClass().getSimpleName());
////            log.info("parameter value = {}", arg);
////        }
////
////        // proceed()를 호출하여 실제 메서드 실행
////        Object returnObj = proceedingJoinPoint.proceed();
////
////        // 메서드의 리턴값 로깅
////        log.info("return type = {}", returnObj.getClass().getSimpleName());
////        log.info("return value = {}", returnObj);
////
////        return returnObj;
////    }
////    private Method getMethod(ProceedingJoinPoint proceedingJoinPoint) {
////        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
////        return signature.getMethod();
////    }
//}
