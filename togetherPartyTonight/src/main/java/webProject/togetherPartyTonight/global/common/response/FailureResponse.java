package webProject.togetherPartyTonight.global.common.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FailureResponse extends CommonResponse {
    String errorMessage;

    public FailureResponse(String fail, int code, String errorMessage) {
        super(fail, code);
        this.errorMessage = errorMessage;
    }
}
