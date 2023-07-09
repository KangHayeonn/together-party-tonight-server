package webProject.togetherPartyTonight.domain.member.oauth.naver;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import webProject.togetherPartyTonight.domain.member.dto.request.NaverLoginRequestDto;
import webProject.togetherPartyTonight.domain.member.entity.OAuthProvider;
import webProject.togetherPartyTonight.domain.member.oauth.OAuthLoginParam;

@RequiredArgsConstructor
@Getter
public class NaverLoginParam implements OAuthLoginParam {

    private final String authorizationCode;

    private final String redirectUri;

    private final String state;
    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.NAVER;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {

        MultiValueMap<String,String> body = new LinkedMultiValueMap<>();
        body.add("code",authorizationCode);
        body.add("redirect_uri", redirectUri);
        body.add("state", state);
        return body;
    }

    public static NaverLoginParam of(NaverLoginRequestDto naverDto){
        return new NaverLoginParam(naverDto.getAuthorizationCode(), naverDto.getRedirectUri(), naverDto.getState());
    }
}
