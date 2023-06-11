package webProject.togetherPartyTonight.global.common;

import lombok.*;
import org.springframework.http.HttpStatus;
import webProject.togetherPartyTonight.global.error.ErrorCode;

/**
 * 정상 응답 - 넘겨줄 data가 있는 경우
 */
@Getter
public class ResponseWithData extends CommonResponse{
    Object data ;

    public ResponseWithData (String success, int code, Object data) {
        super(success,code);
        this.data=data;
    }

}
