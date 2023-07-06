package webProject.togetherPartyTonight.domain.member.oauth.google;

import lombok.RequiredArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import webProject.togetherPartyTonight.domain.member.dto.request.GoogleLoginRequestDto;
import webProject.togetherPartyTonight.domain.member.entity.OAuthProvider;
import webProject.togetherPartyTonight.domain.member.oauth.OAuthLoginParam;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class GoogleLoginParam implements OAuthLoginParam {

    private final String authorizationCode;

    private final String redirectUri;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.GOOGLE;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String,String> body = new LinkedMultiValueMap<>();

        //구글은 url인코딩 된채로 code를 주기 때문에 decode해서 넣어줘야된다.
        body.add("code", URLDecoder.decode(authorizationCode,StandardCharsets.UTF_8));
        body.add("redirect_uri", redirectUri);
        return body;
    }

    public static GoogleLoginParam of(GoogleLoginRequestDto googleDto){
        return new GoogleLoginParam(URLDecoder.decode(googleDto.getAuthorizationCode(), StandardCharsets.UTF_8),googleDto.getRedirectUri());
    }
}
