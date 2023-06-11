package webProject.togetherPartyTonight.global.error;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import webProject.togetherPartyTonight.domain.club.info.exception.ClubException;
import webProject.togetherPartyTonight.domain.user.exception.UserException;

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

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> memberException (UserException e) {
        logger.error("user exception : {}",e.getErrorCode().getMessage());
        return ErrorResponse.toResponse(e.getErrorCode());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> clubException (ClubException e) {
        logger.error("club exception : {}",e.getErrorCode().getMessage());
        return ErrorResponse.toResponse(e.getErrorCode());
    }
}
