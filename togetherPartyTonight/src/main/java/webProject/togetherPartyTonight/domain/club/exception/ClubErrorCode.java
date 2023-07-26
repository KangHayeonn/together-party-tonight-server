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
    CANNOT_WITHDRAW(HttpStatus.BAD_REQUEST, "수락 대기중이거나 수락된 상태에서만 취소할 수 있습니다."),
    CANNOT_SIGNUP_TO_MY_CLUB(HttpStatus.BAD_REQUEST,"내가 만든 모임에는 신청할 수 없습니다."),
    ALREADY_SIGNUP(HttpStatus.BAD_REQUEST, "이미 가입 신청을 했습니다."),
    EXCEED_CLUB_MAXIMUM(HttpStatus.NOT_ACCEPTABLE, "최대 모임 인원을 초과하여 더이상 수락할 수 없습니다."),

    INVALID_CATEGORY (HttpStatus.BAD_REQUEST,"잘못된 카테고리입니다.");

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
