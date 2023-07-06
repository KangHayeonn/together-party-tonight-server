package webProject.togetherPartyTonight.domain.member.oauth.google;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import webProject.togetherPartyTonight.domain.member.entity.OAuthProvider;
import webProject.togetherPartyTonight.domain.member.oauth.OAuthApiClient;
import webProject.togetherPartyTonight.domain.member.oauth.OAuthInfoResponse;
import webProject.togetherPartyTonight.domain.member.oauth.OAuthLoginParam;
import webProject.togetherPartyTonight.domain.member.oauth.kakao.KakaoInfoResponse;
import webProject.togetherPartyTonight.domain.member.oauth.kakao.KakaoTokens;

@Component
@Slf4j
@RequiredArgsConstructor
public class GoogleApiClient implements OAuthApiClient {

    private static final String GRANT_TYPE = "authorization_code";

    @Value("${oauth.google.url.auth}")
    private String authUrl;

    @Value("${oauth.google.url.api}")
    private String apiUrl;

    @Value("${oauth.google.client-id}")
    private String clientId;

    @Value("${oauth.google.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;
    @Override
    public String requestAccessToken(OAuthLoginParam params) {
        String url = authUrl;

        HttpHeaders httpHeaders = new HttpHeaders();

        MultiValueMap<String, String> body = params.makeBody();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", clientId);
        body.add("client_secret",clientSecret);

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<GoogleTokens> response = restTemplate.exchange(url, HttpMethod.POST,request,GoogleTokens.class);
        log.info("구글로그인 토큰 - {}",response.getBody().getAccessToken());
        return response.getBody().getAccessToken();
    }

    @Override
    public OAuthInfoResponse requestOauthInfo(String accessToken) {
        String url = apiUrl + "/v1/userinfo";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + accessToken);
        HttpEntity<?> request = new HttpEntity<>(httpHeaders);

        //구글은 요청을 get으로 한다.
        ResponseEntity<GoogleInfoResponse> response = restTemplate.exchange(url,HttpMethod.GET,request,GoogleInfoResponse.class);

        log.info("구글 사람 정보 - {}", response.getBody());
        return response.getBody();
    }

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.GOOGLE;
    }
}
