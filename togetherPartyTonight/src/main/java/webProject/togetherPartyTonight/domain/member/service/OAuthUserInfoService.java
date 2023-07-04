package webProject.togetherPartyTonight.domain.member.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import webProject.togetherPartyTonight.domain.member.entity.OAuthProvider;
import webProject.togetherPartyTonight.domain.member.oauth.OAuthApiClient;
import webProject.togetherPartyTonight.domain.member.oauth.OAuthInfoResponse;
import webProject.togetherPartyTonight.domain.member.oauth.OAuthLoginParam;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OAuthUserInfoService {

    private final Map<OAuthProvider, OAuthApiClient> clients;

    public OAuthUserInfoService(List<OAuthApiClient> clients) {
        //OAuthApiClient 타입의 빈들을 다 주입해서 카카오, 네이버, 구글 각각의 APiClent를 주입받아서 쓰면 중복 코드많아져서 줄이기 위해
        this.clients = clients.stream().collect(
                Collectors.toUnmodifiableMap(OAuthApiClient::oAuthProvider, Function.identity())
        );
    }

    public OAuthInfoResponse request(OAuthLoginParam params) {
        OAuthApiClient client = clients.get(params.oAuthProvider());
        String accessToken = client.requestAccessToken(params);
        return client.requestOauthInfo(accessToken);
    }
}
