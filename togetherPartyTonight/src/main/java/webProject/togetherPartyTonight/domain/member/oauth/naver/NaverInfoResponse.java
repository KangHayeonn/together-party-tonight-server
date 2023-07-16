package webProject.togetherPartyTonight.domain.member.oauth.naver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import webProject.togetherPartyTonight.domain.member.entity.OAuthProvider;
import webProject.togetherPartyTonight.domain.member.oauth.OAuthInfoResponse;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverInfoResponse implements OAuthInfoResponse {

    private Response response;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response{

        private String nickname;
        private String email;
        @JsonProperty(value = "profile_image")
        private String profileImage;
    }

    @Override
    public String getEmail() {
        return response.getEmail();
    }

    @Override
    public String getNickname() {
        return response.getNickname();
    }

    @Override
    public String getProfileImage() {
        return response.getProfileImage();
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.NAVER;
    }
}
