package webProject.togetherPartyTonight.domain.member.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.*;

@Data
@ApiModel("닉네임변경")
@AllArgsConstructor
@NoArgsConstructor
public class MemberNicknameModifyDto {
    
    private String nickname;
}
