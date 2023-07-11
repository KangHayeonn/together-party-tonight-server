package webProject.togetherPartyTonight.global.error;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.messaging.handler.annotation.support.MethodArgumentTypeMismatchException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import webProject.togetherPartyTonight.domain.club.exception.ClubException;
import webProject.togetherPartyTonight.domain.member.exception.MemberException;
import webProject.togetherPartyTonight.domain.review.exception.ReviewException;
import webProject.togetherPartyTonight.global.common.ErrorResponse;
import webProject.togetherPartyTonight.global.common.response.FailureResponse;
import webProject.togetherPartyTonight.global.common.service.ResponseService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import static org.springframework.data.crossstore.ChangeSetPersister.*;


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


    @ExceptionHandler(CommonException.class)
    public FailureResponse commonException (CommonException e) {
        e.printStackTrace();
        log.error("commonException exception : {}", e.getErrorInterface().getErrorMessage());
        return responseService.getFailureResponse(e.getErrorInterface().getStatusCode(), e.getErrorInterface().getErrorMessage());
    }

    /**
     *
     * request body에서 valid에 걸리는 경우
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public FailureResponse fieldValueException (MethodArgumentNotValidException e) {
        e.printStackTrace();
        StringBuilder sb = new StringBuilder();
        for (FieldError fe : e.getFieldErrors()) {
            sb.append(fe.getDefaultMessage()).append("  ");
        }
        return responseService.getFailureResponse(400, sb.toString());
    }

    /**
     * request body json 파싱시 데이터 타입이 맞지 않는 경우
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public FailureResponse httpMessageNotReadableException (HttpMessageNotReadableException e) {
        e.printStackTrace();
        String[] split = e.getMessage().split("\\[");
        String parameter = split[2].substring(1, split[2].length() - 3);
        //잘못된 형식의 파라미터 이름을 얻기 위한 파싱
        return responseService.getFailureResponse(400, parameter+"의 형식이 잘못되었습니다.");
    }

    /**
     * NotNull 조건에 걸리는 경우
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public FailureResponse missingServletRequestParameterException (MissingServletRequestParameterException e) {
        e.printStackTrace();
        String[] split = e.getMessage().split("'");
        return responseService.getFailureResponse(400, split[1]+"은 null이 될 수 없습니다.");
    }

    /**
     * query parameter의 data type이 맞지않는 경우
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public FailureResponse methodArgumentTypeMismatchException (MethodArgumentTypeMismatchException e) {
        e.printStackTrace();
        return responseService.getFailureResponse(400, "쿼리 파라미터의 데이터 타입이 올바르지 않습니다.");
    }

    /**
     * query parameter에서 valid에 걸리는 경우
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public FailureResponse constraintViolationException (ConstraintViolationException e) {
        e.printStackTrace();
        String[] split = e.getMessage().split(":");
        return responseService.getFailureResponse(400, split[1].substring(1));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public FailureResponse handle404(NoHandlerFoundException ex) {

        return responseService.getFailureResponse(404,"잘못된 경로입니다.");
    }
}
