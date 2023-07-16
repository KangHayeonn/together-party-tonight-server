package webProject.togetherPartyTonight.domain.member.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginResponseDto {
    private Long userId;

    private String accessToken;

    private String refreshToken;

}
