package webProject.togetherPartyTonight.domain.chat.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import webProject.togetherPartyTonight.global.error.ErrorInterface;

@Getter
@AllArgsConstructor
public enum ChatErrorCode implements ErrorInterface {

    //400 BAD_REQUEST

    //409 CONFLICT
    ALREADY_CHATROOM_EXIST (HttpStatus.CONFLICT,"이미 채팅방이 존재합니다");

    //500 INTERNAL_SERVER_ERROR

    //403 FORBIDDEN

    //401 UNAUTHORIZED

    /**
     * 필요한 에러코드 작성
     */


    private final HttpStatus httpStatus;
    private final String message;


    @Override
    public int getStatusCode() {
        return httpStatus.value();
    }

    @Override
    public String getErrorMessage() {
        return message;
    }
}
