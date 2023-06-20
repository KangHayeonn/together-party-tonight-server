package webProject.togetherPartyTonight.domain.member.dto;

import lombok.Data;

@Data
public class TokenDto {

    private String accessToken;

    private String refreshToken;
}
