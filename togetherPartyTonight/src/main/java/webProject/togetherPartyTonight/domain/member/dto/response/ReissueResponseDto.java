package webProject.togetherPartyTonight.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReissueResponseDto {

    private String accessToken;

    public static ReissueResponseDto from(String accessToken){
        return new ReissueResponseDto(accessToken);
    }

}
