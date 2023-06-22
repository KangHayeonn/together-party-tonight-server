package webProject.togetherPartyTonight.global.error;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import webProject.togetherPartyTonight.domain.club.info.exception.ClubException;
import webProject.togetherPartyTonight.domain.member.exception.MemberException;
import webProject.togetherPartyTonight.global.common.ErrorResponse;

import java.time.format.DateTimeParseException;

/**
 * ExceptionHandler를 통한 예외 처리 클래스
 * 각 exception에 맞게 메서드 작성
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * @param e 예외 종류
     * @return 정형화된 ErrorResponse
     */
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final String FAIL = "false";


    /**
     * 매개변수만 다르고 메서드 내용이 중복되는 부분을 어떻게 고칠지 생각해봐야할 것 같습니다
     */

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ErrorResponse> memberException (MemberException e) {
        e.printStackTrace();
        logger.error("member exception : {}",e.getErrorCode().getMessage());
        ErrorResponse errorResponse = new ErrorResponse(FAIL, e.getErrorCode());
        return ResponseEntity.status(200)
                .body(errorResponse);
    }

    @ExceptionHandler(ClubException.class)
    public ResponseEntity<ErrorResponse> clubException (ClubException e) {
        e.printStackTrace();
        logger.error("club exception : {}",e.getErrorCode().getMessage());
        ErrorResponse errorResponse = new ErrorResponse(FAIL, e.getErrorCode());
        return ResponseEntity.status(200)
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> fieldValueException (MethodArgumentNotValidException e) {
        e.printStackTrace();
        StringBuilder sb = new StringBuilder();
        for (FieldError fe : e.getFieldErrors()) {
            sb.append(fe.getDefaultMessage()).append("  ");
        }
        ErrorResponse errorResponse = new ErrorResponse(FAIL, String.valueOf(sb));
        return ResponseEntity.status(200)
                .body(errorResponse);
    }
  
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> httpMessageNotReadableException (HttpMessageNotReadableException e) {
        e.printStackTrace();
        ErrorResponse errorResponse = new ErrorResponse(FAIL, ErrorCode.INVALID_REQUEST_BODY_PARAMETER_TYPE);
        return ResponseEntity.status(200)
                .body(errorResponse);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErrorResponse> dateParseException (DateTimeParseException e) {
        e.printStackTrace();
        ErrorResponse errorResponse = new ErrorResponse(FAIL, ErrorCode.DATE_PARSING_EXCEPTION);
        return ResponseEntity.status(200)
                .body(errorResponse);
    }




}
