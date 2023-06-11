package webProject.togetherPartyTonight.global.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import webProject.togetherPartyTonight.global.error.ErrorCode;

/**
 * 공통 응답을 위한 클래스 (성공, 실패 동일)
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommonResponse {
    private String success;
    private int code;

}
