package webProject.togetherPartyTonight.domain.member.dto.request;

import lombok.Data;

@Data
public class NaverLoginRequestDto {

    private String authorizationCode;

    private String redirectUri;

    private String state;
}
