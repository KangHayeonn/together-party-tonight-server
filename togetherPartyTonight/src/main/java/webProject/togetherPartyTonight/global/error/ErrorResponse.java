package webProject.togetherPartyTonight.global.error;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {
    /**
     * 정형화된 예외 응답 형식을 위한 클래스
     */
    private LocalDateTime timeStamp;
    private HttpStatus httpStatus;
    private String message;


    public static ResponseEntity<ErrorResponse> toResponse(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .httpStatus(errorCode.getHttpStatus())
                        .message(errorCode.getMessage())
                        .timeStamp(LocalDateTime.now())
                        .build());
    }
}
