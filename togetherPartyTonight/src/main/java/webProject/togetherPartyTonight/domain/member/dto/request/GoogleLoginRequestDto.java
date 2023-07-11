package webProject.togetherPartyTonight.domain.member.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("구글 로그인을 위한 정보")
public class GoogleLoginRequestDto {

    private String authorizationCode;

    private String redirectUri;
}
