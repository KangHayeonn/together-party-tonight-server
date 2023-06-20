package webProject.togetherPartyTonight.domain.member.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TokenWithIdDto {
    private Long userId;

    private String accessToken;

    private String refreshToken;

}
