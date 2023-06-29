package webProject.togetherPartyTonight.domain.chat.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import webProject.togetherPartyTonight.global.error.ErrorInterface;

@Getter
@AllArgsConstructor
public enum ChatErrorCode implements ErrorInterface {

    //400 BAD_REQUEST
    OVER_MAX_LIST_COUNT(HttpStatus.BAD_REQUEST, "채팅 요청 가능 수를 초과하였습니다"),
    OVER_MAX_LENGTH(HttpStatus.BAD_REQUEST, "채팅 최대 글자 수를 초과하였습니다"),
    OVER_CHAT_ROOM_PAGE(HttpStatus.BAD_REQUEST, "더 이상 채팅방이 없습니다"),

    //409 CONFLICT
    ALREADY_CHATROOM_EXIST(HttpStatus.CONFLICT, "이미 채팅방이 존재합니다"),
    CHAT_MEMBER_NOT_FOUND(HttpStatus.CONFLICT, "채팅 회원이 존재하지 않습니다"),
    OTHER_CHAT_MEMBER_NOT_FOUND(HttpStatus.CONFLICT, "상대 채팅 회원이 존재하지 않습니다"),
    CHATROOM_NOT_FOUNT(HttpStatus.CONFLICT, "채팅방이 존재하지 않습니다"),

    //500 INTERNAL_SERVER_ERROR

    //403 FORBIDDEN

    //401 UNAUTHORIZED

    ;

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
