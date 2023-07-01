package webProject.togetherPartyTonight.domain.member.dto.request;

import lombok.Data;

@Data
public class KakaoLoginRequestDto {

    private String authorizationCode;
    private String redirectUri;

}
