package webProject.togetherPartyTonight.domain.member.oauth.kakao;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import webProject.togetherPartyTonight.domain.member.dto.request.KakaoLoginRequestDto;
import webProject.togetherPartyTonight.domain.member.entity.OAuthProvider;
import webProject.togetherPartyTonight.domain.member.oauth.OAuthLoginParam;

@RequiredArgsConstructor
@Getter
public class KakaoLoginParam implements OAuthLoginParam {

    private final String authorizationCode;
    private final String redirectUri;
    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        body.add("redirect_uri",redirectUri);
        return body;
    }

    public static KakaoLoginParam of(KakaoLoginRequestDto kakaoDto){
        return new KakaoLoginParam(kakaoDto.getAuthorizationCode(),kakaoDto.getRedirectUri());
    }
}
