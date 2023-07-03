package webProject.togetherPartyTonight.domain.member.oauth;

import org.springframework.util.MultiValueMap;
import webProject.togetherPartyTonight.domain.member.entity.OAuthProvider;

public interface OAuthLoginParam {

    OAuthProvider oAuthProvider();

    MultiValueMap<String, String> makeBody();
}
