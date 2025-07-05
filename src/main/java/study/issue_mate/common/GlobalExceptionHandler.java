package study.issue_mate.common;


import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import study.issue_mate.exception.CustomException;

@Slf4j
@RestControllerAdvice
@Hidden // Swagger에서 특정 컨트롤러나 메서드 숨김을 위한 어노테이션
public class GlobalExceptionHandler {

    // 400 Bad Request 처리 (유효성 검사 실패)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {
        ApiResponse<?> errorResponse = new ApiResponse<>(
                false,
                ex.getMessage(),
                null
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // 400 Bad Request 처리 (IllegalArgumentException 처리 추가)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiResponse<?> errorResponse = new ApiResponse<>(
                false,
                ex.getMessage(),
                null
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // 404 Not Found 처리 (존재하지 않는 URL 경로 요청 시)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        ApiResponse<?> errorResponse = new ApiResponse<>(
                false,
                ex.getMessage(),
                null
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // 405 Method Not Allowed 처리 (지원하지 않는 메서드)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodNotAllowedException(HttpRequestMethodNotSupportedException ex) {
        ApiResponse<?> errorResponse = new ApiResponse<>(
                false,
                ex.getMessage(),
                null
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    // 500 Internal Server Error 처리 (서버 내부 오류)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGlobalException(Exception ex) {
        log.error("Unhandled exception caught in GlobalExceptionHandler", ex);
        ApiResponse<?> errorResponse = new ApiResponse<>(
                false,
                ex.getMessage(),
                null
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Custom Error 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<?>> handleGlobalException(CustomException ex) {

        return new ResponseEntity<>(ApiResponse.errorResponse(ex.getErrorType()), ex.getErrorType().getStatus());
    }

//    private String ExceptionLogger(){
//
//        log.error("======================= Error in service =======================");
//        log.error("ClassName : {}",className);
//        log.error("Method: {}",methodName);
//        log.error("Args: {}", args);
//        log.error("Exception: {}",throwable.getClass().getName());
//        log.error("Message: {}",throwable.getMessage());
////        log.error(throwable);
//        log.error("================================================================");
//    }
}
