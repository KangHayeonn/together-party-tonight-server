package webProject.togetherPartyTonight.domain.member.oauth.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class KakaoTokens {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_in")
    private String expiresIn;

    @JsonProperty("refresh_token_expires_in")
    private String refreshTokenExpiresIn;

    @JsonProperty("scope")
    private String scope;
}
