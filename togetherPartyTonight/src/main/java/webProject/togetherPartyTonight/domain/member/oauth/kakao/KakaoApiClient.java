package webProject.togetherPartyTonight.domain.member.oauth.kakao;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import webProject.togetherPartyTonight.domain.member.entity.OAuthProvider;
import webProject.togetherPartyTonight.domain.member.oauth.OAuthApiClient;
import webProject.togetherPartyTonight.domain.member.oauth.OAuthInfoResponse;
import webProject.togetherPartyTonight.domain.member.oauth.OAuthLoginParam;

@Component
@RequiredArgsConstructor
public class KakaoApiClient implements OAuthApiClient {

    private static final String GRANT_TYPE = "authorization_code";

    @Value("${oauth.kakao.url.auth}")
    private String authUrl;

    @Value("${oauth.kakao.url.api}")
    private String apiUrl;

    @Value("${oauth.kakao.client-id}")
    private String clientId;

    @Value("${oauth.kakao.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    public OAuthProvider oAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public String requestAccessToken(OAuthLoginParam param) throws RestClientException{
        String url = authUrl + "/oauth/token";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = param.makeBody();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", clientId);
        body.add("client_secret",clientSecret);

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<KakaoTokens> response = restTemplate.postForEntity(url, request, KakaoTokens.class);
        return response.getBody().getAccessToken();

    }

    @Override
    public OAuthInfoResponse requestOauthInfo(String accessToken) throws RestClientException{
        String url = apiUrl + "/v2/user/me";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + accessToken);
        HttpEntity<?> request = new HttpEntity<>(httpHeaders);
        ResponseEntity<KakaoInfoResponse> response= restTemplate.exchange(url, HttpMethod.GET, request, KakaoInfoResponse.class);

        return restTemplate.postForObject(url, request, KakaoInfoResponse.class);
    }
}
