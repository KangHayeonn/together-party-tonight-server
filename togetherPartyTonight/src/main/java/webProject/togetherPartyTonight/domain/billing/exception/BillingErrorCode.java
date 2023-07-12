package webProject.togetherPartyTonight.domain.billing.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import webProject.togetherPartyTonight.global.error.ErrorInterface;

@Getter
@RequiredArgsConstructor
public enum BillingErrorCode implements ErrorInterface {

    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_TOKEN(HttpStatus.FORBIDDEN, "잘못된 토큰입니다."),
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "멤버를 찾을 수 없습니다"),
    CLUB_NOT_FOUNT(HttpStatus.BAD_REQUEST, "모임을 찾을 수 없습니다"),
    NOT_MASTER(HttpStatus.BAD_REQUEST, "모임장만이 정산 요청을 할 수 있습니다"),
    MEMBER_NOT_CLUB_MEMBER(HttpStatus.BAD_REQUEST, "클럽에 해당 멤버가 없습니다"),
    BILLING_HISTORY_NOT_FOUNT(HttpStatus.BAD_REQUEST, "정산에 해당하는 정산내역이 없습니다"),
    BILLING_HISTORY_MEMBER_DIFFERENT(HttpStatus.BAD_REQUEST, "정산내역에 해당하는 멤버와 일치하지 않습니다"),
    BILLING_HISTORY_ALREADY_PAYED(HttpStatus.BAD_REQUEST, "이미 결제 완료한 내역입니다");
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
