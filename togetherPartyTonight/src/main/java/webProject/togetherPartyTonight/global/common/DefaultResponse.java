package webProject.togetherPartyTonight.global.common;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class DefaultResponse<T> {

    private HttpStatus httpStatus;
    private String message;
    private T data;

    /* 응답 데이터가 없는 경우 */
    public DefaultResponse(HttpStatus httpStatus, String message) {
        this.httpStatus=httpStatus;
        this.message=message;
        this.data=null;
    }

    /* 응답 데이터가 있는 경우 */
    public DefaultResponse(HttpStatus httpStatus, String message, T data) {
        this.httpStatus=httpStatus;
        this.message=message;
        this.data= data;
    }
}
