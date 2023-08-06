package webProject.togetherPartyTonight.global.common;

import lombok.Getter;
import webProject.togetherPartyTonight.global.common.response.CommonResponse;

/**
 * 예외/에러가 발생했을 경우 응답
 */
@Getter
public class ErrorResponse extends CommonResponse {

    String errorMessage ;
    public ErrorResponse(String success, int code, String errorMessage ) {
        super(success, code);
        this.errorMessage= errorMessage;
    }

}
