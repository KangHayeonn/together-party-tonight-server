package webProject.togetherPartyTonight.global.error;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import webProject.togetherPartyTonight.domain.club.info.exception.ClubException;
import webProject.togetherPartyTonight.domain.member.exception.MemberException;
import webProject.togetherPartyTonight.global.common.ErrorResponse;

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
    private final String FAIL = "fail";


    /**
     * 매개변수만 다르고 메서드 내용이 중복되는 부분을 어떻게 고칠지 생각해봐야할 것 같습니다
     */

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> memberException (MemberException e) {
        logger.error("member exception : {}",e.getErrorCode().getMessage());
        ErrorResponse errorResponse = new ErrorResponse(FAIL, e.getErrorCode());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> clubException (ClubException e) {
        logger.error("club exception : {}",e.getErrorCode().getMessage());
        ErrorResponse errorResponse = new ErrorResponse(FAIL, e.getErrorCode());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(errorResponse);
    }
}
