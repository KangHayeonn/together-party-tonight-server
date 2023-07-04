package webProject.togetherPartyTonight.domain.member.oauth;

import webProject.togetherPartyTonight.domain.member.entity.OAuthProvider;

public interface OAuthInfoResponse {
    String getEmail();
    String getNickname();

    String getProfileImage();
    OAuthProvider getOAuthProvider();
}
