package webProject.togetherPartyTonight.global.common;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import webProject.togetherPartyTonight.global.error.ErrorCode;

import java.time.LocalDateTime;

/**
 * 예외/에러가 발생했을 경우 응답
 */
@Getter
public class ErrorResponse extends CommonResponse{

    String errorMessage ;
    public ErrorResponse(String success, ErrorCode errorCode) {
        super(success, errorCode.getHttpStatus().value());
        this.errorMessage= errorCode.getMessage();
    }

    /**
     *
     * @param success FAIL
     * @param validation request dto의 validation default error message
     */
    public ErrorResponse(String success, String validation) {
        super(success, 400);
        this.errorMessage= validation;
    }

}
