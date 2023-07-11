package webProject.togetherPartyTonight.domain.member.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("카카오 로그인을 위한 정보")
public class KakaoLoginRequestDto {

    private String authorizationCode;
    private String redirectUri;

}
