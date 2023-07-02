package webProject.togetherPartyTonight.domain.club.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import webProject.togetherPartyTonight.global.error.ErrorInterface;

@Getter
@AllArgsConstructor
public enum ClubErrorCode implements ErrorInterface {
    INVALID_CLUB_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 모임 ID 입니다."),
    INVALID_CLUB_SIGNUP_ID(HttpStatus.NOT_FOUND, "유효하지 않은 모임 가입 ID 입니다."),
    INVALID_CLUB_MAXIMUM(HttpStatus.BAD_REQUEST, "현재 멤버 수보다 모임 최대 인원이 적습니다. 더 높게 입력해야 합니다."),
    ALREADY_APPROVED(HttpStatus.CONFLICT, "이미 수락/거절한 사람입니다"),

    ALREADY_SIGNUP(HttpStatus.BAD_REQUEST, "이미 가입 신청을 했습니다.");

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
