package webProject.togetherPartyTonight.domain.member.oauth;

import webProject.togetherPartyTonight.domain.member.entity.OAuthProvider;

public interface OAuthApiClient {
    String requestAccessToken(OAuthLoginParam params);
    OAuthInfoResponse requestOauthInfo(String accessToken);
    OAuthProvider oAuthProvider();
}
