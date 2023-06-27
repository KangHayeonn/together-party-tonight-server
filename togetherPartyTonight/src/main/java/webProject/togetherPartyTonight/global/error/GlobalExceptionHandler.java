package webProject.togetherPartyTonight.global.error;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import webProject.togetherPartyTonight.domain.club.exception.ClubException;
import webProject.togetherPartyTonight.domain.member.exception.MemberException;
import webProject.togetherPartyTonight.domain.review.exception.ReviewException;
import webProject.togetherPartyTonight.global.common.ErrorResponse;
import webProject.togetherPartyTonight.global.common.response.FailureResponse;
import webProject.togetherPartyTonight.global.common.service.ResponseService;

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
    private ResponseService responseService;

    @Autowired
    public GlobalExceptionHandler(ResponseService responseService) {
        this.responseService = responseService;
    }

    /**
     * 매개변수만 다르고 메서드 내용이 중복되는 부분을 어떻게 고칠지 생각해봐야할 것 같습니다
     */

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ErrorResponse> memberException (MemberException e) {
        e.printStackTrace();
        logger.error("member exception : {}",e.getErrorCode().getMessage());
        ErrorResponse errorResponse = new ErrorResponse(FAIL, e.getErrorCode().getHttpStatus().value(), e.getErrorCode().getMessage());
        return ResponseEntity.status(200)
                .body(errorResponse);
    }

    @ExceptionHandler(ClubException.class)
    public FailureResponse clubException (ClubException e) {
        e.printStackTrace();
        logger.error("club exception : {}",e.getErrorInterface().getErrorMessage());
        return responseService.getFailureResponse(e.getErrorInterface().getStatusCode(), e.getErrorInterface().getErrorMessage());
    }

    @ExceptionHandler(ReviewException.class)
    public FailureResponse reviewException (ReviewException e) {
        e.printStackTrace();
        logger.error("review exception : {}",e.getErrorInterface().getErrorMessage());
        return responseService.getFailureResponse(e.getErrorInterface().getStatusCode(), e.getErrorInterface().getErrorMessage());

    }

    @ExceptionHandler(CommonException.class)
    public FailureResponse commonException (CommonException e) {
        e.printStackTrace();
        log.error("commonException exception : {}", e.getErrorInterface().getErrorMessage());
        return responseService.getFailureResponse(e.getErrorInterface().getStatusCode(), e.getErrorInterface().getErrorMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public FailureResponse fieldValueException (MethodArgumentNotValidException e) {
        e.printStackTrace();
        StringBuilder sb = new StringBuilder();
        for (FieldError fe : e.getFieldErrors()) {
            sb.append(fe.getDefaultMessage()).append("  ");
        }
        return responseService.getFailureResponse(400, sb.toString());
    }
  
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public FailureResponse httpMessageNotReadableException (HttpMessageNotReadableException e) {
        e.printStackTrace();
        return responseService.getFailureResponse(ErrorCode.INVALID_REQUEST_BODY_PARAMETER_TYPE.getHttpStatus().value(), ErrorCode.INVALID_REQUEST_BODY_PARAMETER_TYPE.getMessage());
    }

    @ExceptionHandler(DateTimeParseException.class)
    public FailureResponse dateParseException (DateTimeParseException e) {
        e.printStackTrace();
        return responseService.getFailureResponse(ErrorCode.DATE_PARSING_EXCEPTION.getHttpStatus().value(), ErrorCode.DATE_PARSING_EXCEPTION.getMessage());
    }




}
