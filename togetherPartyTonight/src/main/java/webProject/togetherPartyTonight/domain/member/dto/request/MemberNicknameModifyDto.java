package webProject.togetherPartyTonight.domain.member.dto.request;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@ApiModel("닉네임변경")
@AllArgsConstructor
public class MemberNicknameModifyDto {
    
    private String nickname;
}
