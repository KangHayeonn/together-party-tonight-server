package webProject.togetherPartyTonight.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReissueResponseDto {

    private String accessToken;

    private String refreshToken;

    public static ReissueResponseDto from(String accessToken,String refreshToken){
        return new ReissueResponseDto(accessToken,refreshToken);
    }

}
