package webProject.togetherPartyTonight.domain.member.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("네이버 로그인을 위한 정보")
public class NaverLoginRequestDto {

    private String authorizationCode;

    private String redirectUri;

    private String state;
}
