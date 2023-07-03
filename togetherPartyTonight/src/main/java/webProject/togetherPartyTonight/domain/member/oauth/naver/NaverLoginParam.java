package webProject.togetherPartyTonight.domain.member.oauth.naver;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import webProject.togetherPartyTonight.domain.member.entity.OAuthProvider;
import webProject.togetherPartyTonight.domain.member.oauth.OAuthLoginParam;

@Getter
@RequiredArgsConstructor
public class NaverLoginParam implements OAuthLoginParam {

    private final String authorizationCode;
    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.NAVER;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        return null;
    }
}
